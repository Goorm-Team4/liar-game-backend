package com.goorm.liargame.game.dto.response;

import com.goorm.liargame.game.enums.Player;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LiarAnswerRespDto {

    private boolean correct;
    private Player winner;

    public LiarAnswerRespDto(boolean correct, Player winner) {
        this.correct = correct;
        this.winner = winner;
    }
}
