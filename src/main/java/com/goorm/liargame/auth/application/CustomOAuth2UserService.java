package com.goorm.liargame.auth.application;


import com.goorm.liargame.auth.domain.OAuth2UserImpl;
import com.goorm.liargame.auth.dto.OAuthAttributes;
import com.goorm.liargame.member.domain.Member;
import com.goorm.liargame.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = service.loadUser(userRequest);  // OAuth2 정보 조회

		Map<String, Object> originAttributes = oAuth2User.getAttributes();  // OAuth2User의 attribute

		// OAuth2 서비스 id (kakao, ...)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// OAuth2User의 attribute를 서비스 유형에 맞게 변환
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, originAttributes);
		Member member = getOrSave(attributes);

		return new OAuth2UserImpl(originAttributes, member);
	}

	private Member getOrSave(OAuthAttributes authAttributes) {
		return memberRepository.findBySocialId(authAttributes.getSocialId())
			.orElseGet(() -> memberRepository.save(authAttributes.toEntity()));
	}
}
