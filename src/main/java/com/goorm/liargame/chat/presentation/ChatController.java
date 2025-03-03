package com.goorm.liargame.chat.presentation;

import com.goorm.liargame.chat.dto.request.ChatMessageReqDto;
import com.goorm.liargame.chat.dto.request.MessageType;
import com.goorm.liargame.chat.dto.response.ChatMessageRespDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

    /**
     * 채팅 메시지를 받아서 전송하는 메서드(턴/채팅/최종 메시지에 사용)
     */
    @MessageMapping("/games/{gameId}/messages") // 메시지 보내는 주소 -> /pub/games/{gameId}/messages
    @SendTo("/sub/games/{gameId}/messages") // 메시지 받는 주소 -> /sub/games/{gameId}/messages
    public ChatMessageRespDto sendMessage(ChatMessageReqDto request,
                                          @DestinationVariable String gameId) {
        Long nextTurnUsername = null;
        if (request.getType() == MessageType.TURN) {
            // TODO: 다음 순서 유저 할당
        }

        return ChatMessageRespDto.builder()
                .username(request.getUsername())
                .content(request.getContent())
                .type(request.getType())
                .nextTurnUsername(nextTurnUsername)
                .build();
    }
}
