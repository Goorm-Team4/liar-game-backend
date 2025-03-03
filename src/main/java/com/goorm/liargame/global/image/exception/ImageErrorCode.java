package com.goorm.liargame.global.image.exception;

import com.goorm.liargame.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements BaseErrorCode {

    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;
}
