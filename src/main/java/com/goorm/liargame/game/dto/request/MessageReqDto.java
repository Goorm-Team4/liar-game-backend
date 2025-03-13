package com.goorm.liargame.game.dto.request;

import com.goorm.liargame.game.dto.PlayerInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReqDto {

    private PlayerInfo player;
    private String content;
}
