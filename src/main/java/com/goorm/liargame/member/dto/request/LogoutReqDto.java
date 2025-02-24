package com.goorm.liargame.member.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LogoutReqDto {

    private String username;
    private String token;

    public LogoutReqDto(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
