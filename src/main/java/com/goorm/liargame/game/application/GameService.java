package com.goorm.liargame.game.application;

import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.response.ChatMessageRespDto;
import com.goorm.liargame.game.dto.response.LiarAnswerRespDto;
import com.goorm.liargame.game.dto.response.TurnMessageRespDto;
import com.goorm.liargame.game.enums.Player;
import com.goorm.liargame.global.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class GameService {

    private final RedisUtil redisUtil;

    public TurnMessageRespDto sendTurnMessage(String gameId, MessageReqDto request) {
        String KEY = "game:" + gameId;
        String ORDER = "order";

        Long nextPlayerId = null;
        boolean lastPlayer = false;

//        Map<String, Object> game = new HashMap<>();
//        game.put("word", "사과");
//        game.put("order", new ArrayList<>(List.of(1L, 2L, 3L, 4L)));
//        redisUtil.setValue(KEY, game);

        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(KEY));
        List<Long> order = (List<Long>) game.get(ORDER);

        int currentPlayerIdx = order.indexOf(request.getPlayerId());
        if (currentPlayerIdx == order.size() - 1) {
            lastPlayer = true;
        } else {
            nextPlayerId = order.get(currentPlayerIdx + 1);
        }

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

    public LiarAnswerRespDto verifyLiarAnswer(String gameId, LiarAnswerReqDto request) {
        String KEY = "game:" + gameId;
        String WORD = "word";

        boolean correct = false;

        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(KEY));
        String word = (String) game.get(WORD);

        if (word.equals(request.getAnswer())) {
            correct = true;
        }

        Player winner = correct ? Player.LIAR : Player.NORMAL;

        return new LiarAnswerRespDto(correct, winner);
    }
}
