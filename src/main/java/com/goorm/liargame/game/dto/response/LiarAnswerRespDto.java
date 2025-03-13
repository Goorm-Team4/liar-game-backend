package com.goorm.liargame.game.dto.response;

import com.goorm.liargame.game.dto.PlayerInfo;
import com.goorm.liargame.game.enums.PlayerType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LiarAnswerRespDto {

    private boolean correct;
    private PlayerType winner;
    private PlayerInfo liar;
    private List<PlayerInfo> nomals;

    @Builder
    public LiarAnswerRespDto(boolean correct, PlayerType winner, PlayerInfo liar, List<PlayerInfo> nomals) {
        this.correct = correct;
        this.winner = winner;
        this.liar = liar;
        this.nomals = nomals;
    }
}
