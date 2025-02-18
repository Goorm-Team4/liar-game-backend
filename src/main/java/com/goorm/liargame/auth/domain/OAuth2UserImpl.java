package com.goorm.liargame.auth.domain;

import com.goorm.liargame.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
public class OAuth2UserImpl implements OAuth2User, Serializable {

	private Map<String, Object> attributes;
	private Member member;

	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getName() {
		return member.getUsername();
	}
}
