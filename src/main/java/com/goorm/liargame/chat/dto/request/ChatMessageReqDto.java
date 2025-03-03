package com.goorm.liargame.chat.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageReqDto {

    private Long userId;
    private String username;
    private String content;
    private MessageType type;
}
