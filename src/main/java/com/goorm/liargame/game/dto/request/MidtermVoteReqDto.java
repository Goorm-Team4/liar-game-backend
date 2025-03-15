package com.goorm.liargame.game.dto.request;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MidtermVoteReqDto {
    private String gameId;
    private String playerId;
    private String votePlayerId;
}
