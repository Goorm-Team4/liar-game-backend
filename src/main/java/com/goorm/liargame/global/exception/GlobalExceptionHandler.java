package com.goorm.liargame.global.exception;


import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.goorm.liargame.auth.presentation.AuthController;
import com.goorm.liargame.global.common.dto.ApiResponse;
import com.goorm.liargame.member.presentation.MemberController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class}, basePackageClasses = { MemberController.class, AuthController.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        log.warn("** HttpMessageNotReadable Exception **", e);

        if (cause instanceof ValueInstantiationException exception) {
            Throwable innerCause = exception.getCause(); // 내부 예외 확인
            if (innerCause instanceof BaseException baseException) {
                return baseException.getErrorCode().toResponseEntity();
            }
        }

        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.warn("** Exception **", e);

        HttpStatus status = INTERNAL_SERVER_ERROR;
        return ResponseEntity.internalServerError()
                .body(ApiResponse.failure(status, status.getReasonPhrase()));
    }
}
