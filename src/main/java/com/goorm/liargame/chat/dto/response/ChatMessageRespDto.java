package com.goorm.liargame.chat.dto.response;

import com.goorm.liargame.chat.dto.request.MessageType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRespDto {

    private String username;
    private String content;
    private MessageType type;
    private Long nextTurnUsername;

    @Builder
    public ChatMessageRespDto(String username, String content, MessageType type, Long nextTurnUsername) {
        this.username = username;
        this.content = content;
        this.type = type;
        this.nextTurnUsername = nextTurnUsername;
    }
}
