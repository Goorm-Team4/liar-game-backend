package com.goorm.liargame.game.dto.request;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MidtermVoteReqDto {
    private Long gameId;
    private Long playerId;
    private Long votePlayerId;
}
