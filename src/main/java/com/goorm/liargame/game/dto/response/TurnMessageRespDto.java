package com.goorm.liargame.game.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TurnMessageRespDto {

    private Long playerId;
    private String content;
    private Long nextPlayerId;
    private boolean lastPlayer;

    @Builder
    public TurnMessageRespDto(Long playerId, String content, Long nextPlayerId, boolean lastPlayer) {
        this.playerId = playerId;
        this.content = content;
        this.nextPlayerId = nextPlayerId;
        this.lastPlayer = lastPlayer;
    }
}
