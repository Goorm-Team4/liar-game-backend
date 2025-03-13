package com.goorm.liargame.game.dto.response;

import com.goorm.liargame.game.enums.PlayerType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LiarAnswerRespDto {

    private boolean correct;
    private PlayerType winner;

    public LiarAnswerRespDto(boolean correct, PlayerType winner) {
        this.correct = correct;
        this.winner = winner;
    }
}
