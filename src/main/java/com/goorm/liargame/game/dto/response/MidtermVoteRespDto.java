package com.goorm.liargame.game.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class MidtermVoteRespDto {
    private String playerId;
    private String votePlayerId;

    public MidtermVoteRespDto(String playerId, String votePlayerId) {
        this.playerId = playerId;
        this.votePlayerId = votePlayerId;
    }
}
