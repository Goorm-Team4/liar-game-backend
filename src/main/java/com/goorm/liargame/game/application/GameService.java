package com.goorm.liargame.game.application;

import com.goorm.liargame.game.dto.PlayerInfo;
import com.goorm.liargame.game.dto.request.FinalVoteReqDto;
import com.goorm.liargame.game.dto.request.FinalVoteResultReqDto;
import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.response.*;
import com.goorm.liargame.game.enums.Player;
import com.goorm.liargame.global.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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


    public FinalVoteRespDto sendFinalVote(String gameId, FinalVoteReqDto request) {
        String KEY = "game:" + gameId;
        String FINAL_VOTE = "final-vote";

        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(KEY));
        Map<Long, Boolean> finalVote = (Map<Long, Boolean>) game.get(FINAL_VOTE);
        finalVote.put(request.getVoter().getPlayerId(), request.isKill());
        game.put(FINAL_VOTE, finalVote);

        redisUtil.setValue(KEY, game);

        return new FinalVoteRespDto(request.getVoter(), request.isKill());
    }

    public FinalVoteResultRespDto sendFinalVoteResult(String gameId, FinalVoteResultReqDto request) {
        String KEY = "game:" + gameId;
        String FINAL_VOTE = "final-vote";


        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(KEY));
//        Map<Long, Map<String, String>> aplayers = new HashMap<>();
//        Map<String, String> info = new HashMap<>();
//        info.put("nickname", "test");
//        info.put("profileUrl", "test");
//        aplayers.put(1L, info);
//        aplayers.put(2L, info);
//        aplayers.put(3L, info);
//        aplayers.put(4L, info);
//        aplayers.put(5L, info);
//        game.put("liar", 1L);
//        game.put("players", aplayers);
//        redisUtil.setValue(KEY, game);
        Map<Long, Boolean> finalVote = (Map<Long, Boolean>) game.get(FINAL_VOTE);
        Map<Long, Map<String, String>> players = (Map<Long, Map<String, String>>) game.get("players");

        int size = players.size();
        long killCount = finalVote.values().stream().filter(v -> v).count();

        Long liarId = (Long) game.get("liar");
        boolean isLiar = false;
        PlayerInfo liar = PlayerInfo.from(liarId, players.get(liarId));
        PlayerInfo votedPlayer;

        if (Objects.equals(liarId, request.getVotedPlayerId())) {
            isLiar = true;
            votedPlayer = liar;
        } else {
            votedPlayer = PlayerInfo.from(request.getVotedPlayerId(), players.get(request.getVotedPlayerId()));
        }

        List<PlayerInfo> nomals = players.entrySet().stream()
                .filter(entry -> !Objects.equals(entry.getKey(), liarId)) // 라이어가 아닌 경우에만 필터링
                .map(PlayerInfo::from)
                .toList();

        boolean isKill = killCount >= size / 2;

        return FinalVoteResultRespDto.builder()
                .liar(liar)
                .votedPlayer(votedPlayer)
                .kill(isKill)
                .isLiar(isLiar)
                .normals(nomals)
                .build();
    }
}
