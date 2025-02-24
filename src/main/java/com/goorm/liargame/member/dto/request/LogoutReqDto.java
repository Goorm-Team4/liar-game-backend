package com.goorm.liargame.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogoutReqDto {

    private String username;
    private String token;
}
