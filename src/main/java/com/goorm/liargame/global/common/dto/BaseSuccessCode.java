package com.goorm.liargame.global.common.dto;

import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {

    HttpStatus getStatus();
    String getMessage();
}
