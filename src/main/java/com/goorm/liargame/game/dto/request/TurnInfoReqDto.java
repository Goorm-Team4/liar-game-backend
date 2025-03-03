package com.goorm.liargame.game.dto.request;

import com.goorm.liargame.game.enums.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TurnInfoReqDto {

    private MessageType type;
}
