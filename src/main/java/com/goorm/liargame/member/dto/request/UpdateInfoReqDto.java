package com.goorm.liargame.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateInfoReqDto {
    @Schema(description = "유저네임", example = "김구름")
    private String username;
}
