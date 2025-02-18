package com.goorm.liargame.auth.dto;


import com.goorm.liargame.member.domain.Member;
import com.goorm.liargame.member.domain.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
@Getter
public class OAuthAttributes {

	private final Map<String, Object> attributes;
	private final String nameAttributesKey;
	private final String username;
	private final String email;
	private final String socialId;
	private final SocialType socialType;
	private final String profileImageUrl;

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributesKey, String username, String email,
						   String socialId, SocialType socialType, String profileImageUrl) {
		this.attributes = attributes;
		this.nameAttributesKey = nameAttributesKey;
		this.username = username;
		this.email = email;
		this.socialId = socialId;
		this.socialType = socialType;
		this.profileImageUrl = profileImageUrl;
	}

	public static OAuthAttributes of(String socialName, Map<String, Object> attributes) {
		if ("kakao".equals(socialName)) {
			return ofKakao("id", attributes);
		}

		return null;
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) account.get("profile");

		String socialId = String.valueOf(attributes.get(userNameAttributeName));

		return OAuthAttributes.builder()
			.username(String.valueOf(profile.get("nickname")))
			.profileImageUrl(String.valueOf(profile.get("profile_image_url")))
			.nameAttributesKey(userNameAttributeName)
			.attributes(attributes)
			.socialId(socialId)
			.socialType(SocialType.KAKAO)
			.email(String.valueOf(account.get("email")))
			.build();
	}

	public Member toEntity() {
		return Member.builder()
			.username(username)
			.socialId(socialId)
			.socialType(socialType)
			.email(email)
			.profileImageUrl(profileImageUrl)
			.build();
	}
}
