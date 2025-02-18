package com.goorm.liargame.auth.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenRespDto {
	private String accessToken;

	@Builder
	public TokenRespDto(String accessToken) {
		this.accessToken = accessToken;
	}
}
