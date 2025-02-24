package com.goorm.liargame.global.common.dto;

import com.goorm.liargame.global.exception.BaseErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;


    public static ApiResponse<Void> success(BaseSuccessCode successStatus) {
        return ApiResponse.<Void>builder()
                .status(successStatus.getStatus())
                .message(successStatus.getMessage())
                .build();
    }


    public static<T> ApiResponse<T> success(BaseSuccessCode successStatus, T data) {
        return ApiResponse.<T>builder()
                .status(successStatus.getStatus())
                .message(successStatus.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> failure(BaseErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> failure(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .build();
    }
}
