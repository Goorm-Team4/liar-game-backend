package com.goorm.liargame.auth.exception;


import com.goorm.liargame.global.exception.BaseException;
import lombok.Getter;

@Getter
public class CustomJwtException extends BaseException {

	public CustomJwtException(JwtErrorCode errorCode) {
		super(errorCode);
	}
}
