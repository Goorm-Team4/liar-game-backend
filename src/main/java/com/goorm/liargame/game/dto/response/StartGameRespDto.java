package com.goorm.liargame.game.dto.response;

import com.goorm.liargame.game.enums.PlayerType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartGameRespDto {
    private String message;
    private String gameId;
    private String word;
    private String topic;
    private PlayerType playerType;
}
