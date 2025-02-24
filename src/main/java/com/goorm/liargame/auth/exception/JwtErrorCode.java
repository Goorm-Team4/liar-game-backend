package com.goorm.liargame.auth.exception;


import com.goorm.liargame.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum JwtErrorCode implements BaseErrorCode {

	EXPIRED_TOKEN(UNAUTHORIZED, "토큰이 만료되었습니다."),
	BLACKLISTED_TOKEN(UNAUTHORIZED, "로그아웃된 사용자입니다."),
	INVALID_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다."),
	UNAUTHORIZED_ACCESS(UNAUTHORIZED,  "사용자 인증이 필요합니다."),
	ACCESS_DENIED(FORBIDDEN, "해당 요청에 대한 접근 권한이 없습니다."),
	JWT_ERROR(INTERNAL_SERVER_ERROR, "JWT 에러가 발생하였습니다. 서버 관리자에게 문의주세요.");

	private final HttpStatus status;
	private final String message;
}
