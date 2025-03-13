package com.goorm.liargame.game.dto.response;


import com.goorm.liargame.game.dto.PlayerInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FinalVoteResultRespDto {

    private PlayerInfo liar;
    private PlayerInfo votedPlayer;
    private boolean kill;
    private boolean isLiar;
    private List<PlayerInfo> normals;

    @Builder
    public FinalVoteResultRespDto(PlayerInfo liar, PlayerInfo votedPlayer, boolean kill, boolean isLiar, List<PlayerInfo> normals) {
        this.liar = liar;
        this.votedPlayer = votedPlayer;
        this.kill = kill;
        this.isLiar = isLiar;
        this.normals = normals;
    }
}
