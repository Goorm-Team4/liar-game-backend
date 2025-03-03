package com.goorm.liargame.game.dto.response;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FinalVoteResultRespDto {

    private Long playerId;
    private boolean liar;
}
