package com.goorm.liargame.member.exception;

import com.goorm.liargame.global.exception.BaseException;
import lombok.Getter;

@Getter
public class MemberException extends BaseException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
