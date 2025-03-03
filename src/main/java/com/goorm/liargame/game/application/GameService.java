package com.goorm.liargame.game.application;

import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.response.ChatMessageRespDto;
import com.goorm.liargame.game.dto.response.LiarAnswerRespDto;
import com.goorm.liargame.game.dto.response.TurnMessageRespDto;
import com.goorm.liargame.game.enums.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameService {

    public TurnMessageRespDto sendTurnMessage(MessageReqDto request) {
        Long nextPlayerId = 1L;
        boolean lastPlayer = false;

        // TODO: 다음 플레이어 아이디 및 마지막 플레이어 여부 할당
        return TurnMessageRespDto.builder()
                .playerId(request.getPlayerId())
                .content(request.getContent())
                .nextPlayerId(nextPlayerId)
                .lastPlayer(lastPlayer)
                .build();
    }

    public ChatMessageRespDto sendChatMessage(MessageReqDto request) {
        return new ChatMessageRespDto(request.getPlayerId(), request.getContent());
    }

//    public TurnInfoRespDto sendTurnInfo(TurnInfoReqDto request) {
//        Long userId = 1L;
//        if (request.getType() == MessageType.TURN) {
//            // TODO: 첫번째 순서 플레이어 아이디 리턴
//        } else {
//            // TODO: 예외 처리
//        }
//
//        return new TurnInfoRespDto(userId);
//    }

    public LiarAnswerRespDto verifyLiarAnswer(LiarAnswerReqDto request) {
        // TODO: 라이어 정답 판별
        boolean correct = true;
        Player winner = correct ? Player.LIAR : Player.NORMAL;

        return new LiarAnswerRespDto(correct, winner);
    }
}
