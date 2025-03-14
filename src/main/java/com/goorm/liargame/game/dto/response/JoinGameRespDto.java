package com.goorm.liargame.game.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinGameRespDto {
    private String message;
    private String gameId;
    private String profileImgUrl;
    private String nickname;
    private Long playerId;
}