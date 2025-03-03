package com.goorm.liargame.global.image.exception;

import com.goorm.liargame.global.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ImageException extends BaseException {

    private HttpStatus status;

    public ImageException(ImageErrorCode errorCode) {
        super(errorCode);
    }
}
