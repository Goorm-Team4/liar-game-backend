package com.goorm.liargame.game.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRespDto {

    private Long playerId;
    private String content;


    public ChatMessageRespDto(Long playerId, String content) {
        this.playerId = playerId;
        this.content = content;
    }
}
