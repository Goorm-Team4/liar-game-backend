package com.goorm.liargame.game.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinGameRespDto {
    private String gameId;
    private String message;
}