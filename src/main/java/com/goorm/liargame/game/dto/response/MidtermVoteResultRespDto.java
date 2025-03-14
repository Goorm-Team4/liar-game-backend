package com.goorm.liargame.game.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MidtermVoteResultRespDto {

    private List<Long> votedPlayer;

    @Builder
    public MidtermVoteResultRespDto(List<Long> votedPlayer) {
        this.votedPlayer = votedPlayer;
    }
}
