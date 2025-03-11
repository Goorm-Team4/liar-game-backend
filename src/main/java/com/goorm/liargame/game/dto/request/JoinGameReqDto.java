package com.goorm.liargame.game.dto.request;

import lombok.Data;

@Data
public class JoinGameReqDto {
    private Long playerId;
    private String gameId;
}
