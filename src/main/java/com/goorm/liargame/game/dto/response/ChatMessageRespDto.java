package com.goorm.liargame.game.dto.response;

import com.goorm.liargame.game.dto.PlayerInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRespDto {

    private PlayerInfo playerId;
    private String content;

    public ChatMessageRespDto(PlayerInfo playerId, String content) {
        this.playerId = playerId;
        this.content = content;
    }
}
