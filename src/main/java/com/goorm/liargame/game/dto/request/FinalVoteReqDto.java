package com.goorm.liargame.game.dto.request;

import com.goorm.liargame.game.dto.PlayerInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FinalVoteReqDto {

    private PlayerInfo voter;
    private boolean kill;
}
