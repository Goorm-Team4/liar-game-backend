package com.goorm.liargame.member.exception;

import com.goorm.liargame.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 회원입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
