package com.goorm.liargame.game.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FinalVoteReqDto {

    private Long voterId;
    private boolean kill;
}
