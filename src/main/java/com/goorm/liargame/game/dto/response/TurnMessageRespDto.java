package com.goorm.liargame.game.dto.response;

import com.goorm.liargame.game.dto.PlayerInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TurnMessageRespDto {

    private PlayerInfo player;
    private String content;
    private PlayerInfo nextPlayer;
    private boolean lastPlayer;

    @Builder
    public TurnMessageRespDto(PlayerInfo player, String content, PlayerInfo nextPlayer, boolean lastPlayer) {
        this.player = player;
        this.content = content;
        this.nextPlayer = nextPlayer;
        this.lastPlayer = lastPlayer;
    }
}
