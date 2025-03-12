package com.goorm.liargame.game.dto.response;

import com.goorm.liargame.game.dto.PlayerInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FinalVoteRespDto {
    private PlayerInfo voter;
    private boolean kill;

    public FinalVoteRespDto(PlayerInfo voter, boolean kill) {
        this.voter = voter;
        this.kill = kill;
    }
}
