package com.goorm.liargame.game.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class MidtermVoteRespDto {
    private Long playerId;
    private Long votePlayerId;

    public MidtermVoteRespDto(Long playerId, Long votePlayerId) {
        this.playerId = playerId;
        this.votePlayerId = votePlayerId;
    }
}
