package com.goorm.liargame.member.success;

import com.goorm.liargame.global.common.dto.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MemberSuccessCode implements BaseSuccessCode {
    DELETE_MEMBER_SUCCESS("회원 탈퇴에 성공하였습니다."),
    UPDATE_MEMBER_SUCCESS("회원 정보 수정에 성공하였습니다."),;

    private final HttpStatus status = HttpStatus.OK;
    private final String message;
}
