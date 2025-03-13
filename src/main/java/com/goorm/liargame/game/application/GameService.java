package com.goorm.liargame.game.application;

import com.goorm.liargame.game.dto.PlayerInfo;
import com.goorm.liargame.game.dto.request.FinalVoteReqDto;
import com.goorm.liargame.game.dto.request.FinalVoteResultReqDto;
import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.response.*;
import com.goorm.liargame.game.enums.PlayerType;
import com.goorm.liargame.global.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class GameService {

    private final RedisUtil redisUtil;

    public static String GAME_PREFIX = "game:";
    public static String PLAYERS_KEY = "players";
    public static String FINAL_VOTE_KEY = "final-vote";
    public static String WORD_KEY = "word";
    public static String LIAR_KEY = "liar";
    public static String ORDER_KEY = "order";

    public TurnMessageRespDto sendTurnMessage(String gameId, MessageReqDto request) {
        String GAME_KEY = GAME_PREFIX + gameId;

        Long nextPlayerId = null;
        boolean lastPlayer = false;

//        Map<String, Object> new_game = new HashMap<>();
//        new_game.put("word", "사과");
//        new_game.put("order", new ArrayList<>(List.of(1L, 2L, 3L, 4L)));
//        redisUtil.setValue(GAME_KEY, new_game);

        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
        List<Long> order = (List<Long>) game.get(ORDER_KEY);
        Map<Long, Map<String, String>> players = (Map<Long, Map<String, String>>) game.get("players");

        int currentPlayerIdx = order.indexOf(request.getPlayer().getPlayerId());
        if (currentPlayerIdx == order.size() - 1) {
            lastPlayer = true;
        } else {
            nextPlayerId = order.get(currentPlayerIdx + 1);
        }

        PlayerInfo nextPlayer = PlayerInfo.from(nextPlayerId, players.get(nextPlayerId.toString()));

        return TurnMessageRespDto.builder()
                .player(request.getPlayer())
                .content(request.getContent())
                .nextPlayer(nextPlayer)
                .lastPlayer(lastPlayer)
                .build();
    }

    public ChatMessageRespDto sendChatMessage(MessageReqDto request) {
        return new ChatMessageRespDto(request.getPlayer(), request.getContent());
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
        String GAME_KEY = GAME_PREFIX + gameId;

        boolean correct = false;

        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
        String word = (String) game.get(WORD_KEY);

        if (word.equals(request.getAnswer())) {
            correct = true;
        }

        PlayerType winner = correct ? PlayerType.LIAR : PlayerType.NORMAL;

        Long liarId = (Long) game.get(LIAR_KEY);
        Map<String, Map<String, String>> rawPlayers = (Map<String, Map<String, String>>) game.get(PLAYERS_KEY);
        Map<Long, Map<String, String>> players = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : rawPlayers.entrySet()) {
            players.put(Long.parseLong(entry.getKey()), entry.getValue());
        }
        PlayerInfo liar = PlayerInfo.from(liarId, players.get(liarId));
        List<PlayerInfo> nomals = players.entrySet().stream()
                .filter(entry -> !Objects.equals(entry.getKey(), liarId)) // 라이어가 아닌 경우에만 필터링
                .map(PlayerInfo::from)
                .toList();

        return LiarAnswerRespDto.builder()
                .correct(correct)
                .winner(winner)
                .liar(liar)
                .nomals(nomals)
                .build();
    }


    public FinalVoteRespDto sendFinalVote(String gameId, FinalVoteReqDto request) {
        String GAME_KEY = GAME_PREFIX + gameId;

        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
        Map<Long, Boolean> finalVote = (Map<Long, Boolean>) game.get(FINAL_VOTE_KEY);
        finalVote.put(request.getVoter().getPlayerId(), request.isKill());
        game.put(FINAL_VOTE_KEY, finalVote);

        redisUtil.setValue(GAME_KEY, game);

        return new FinalVoteRespDto(request.getVoter(), request.isKill());
    }

    public FinalVoteResultRespDto sendFinalVoteResult(String gameId, FinalVoteResultReqDto request) {
        String GAME_KEY = GAME_PREFIX + gameId;

        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
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
//        redisUtil.setValue(GAME_KEY, game);
        Map<Long, Boolean> finalVote = (Map<Long, Boolean>) game.get(FINAL_VOTE_KEY);
        Map<String, Map<String, String>> rawPlayers = (Map<String, Map<String, String>>) game.get(PLAYERS_KEY);
        Map<Long, Map<String, String>> players = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : rawPlayers.entrySet()) {
            players.put(Long.parseLong(entry.getKey()), entry.getValue());
        }

        int size = players.size();
        long killCount = finalVote.values().stream().filter(v -> v).count();

        Long liarId = (Long) game.get(LIAR_KEY);
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
