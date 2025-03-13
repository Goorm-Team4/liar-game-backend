package com.goorm.liargame.game.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartGameReqDto {
    private String gameId;
    private Long playerId;
}
