package com.goorm.liargame.global.exception;

import com.goorm.liargame.global.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface BaseErrorCode {

    HttpStatus getStatus();
    String getMessage();

    default ApiResponse<Void> toApiResponse() {
        return ApiResponse.failure(this);
    }

    default ResponseEntity<ApiResponse<Void>> toResponseEntity() {
        return ResponseEntity
                .status(getStatus())
                .body(toApiResponse());
    }
}
