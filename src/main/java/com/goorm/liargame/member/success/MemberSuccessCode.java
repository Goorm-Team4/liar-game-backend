package com.goorm.liargame.member.success;

import com.goorm.liargame.global.common.dto.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MemberSuccessCode implements BaseSuccessCode {
    LOGOUT_SUCCESS("로그아웃에 성공하였습니다.");

    private final HttpStatus status = HttpStatus.OK;
    private final String message;
}
