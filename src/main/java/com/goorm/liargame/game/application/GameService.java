package com.goorm.liargame.game.application;

import com.goorm.liargame.game.dto.request.DeleteGameReqDto;
import com.goorm.liargame.game.dto.request.JoinGameReqDto;
import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.response.ChatMessageRespDto;
import com.goorm.liargame.game.dto.response.CreateGameRespDto;
import com.goorm.liargame.game.dto.response.JoinGameRespDto;
import com.goorm.liargame.game.dto.response.LiarAnswerRespDto;
import com.goorm.liargame.game.dto.response.TurnMessageRespDto;
import com.goorm.liargame.game.enums.Player;
import com.goorm.liargame.global.common.utils.RedisUtil;
import com.goorm.liargame.member.application.MemberService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameService {

    private final RedisUtil redisUtil;
    private final String REDIS_GAME_PREFIX = "game:";
    private final String REDIS_GAME_TURN_PREFIX = "turn:";
    private final String REDIS_GAME_CHAT_PREFIX = "chat:";
    private final String BASE_JOIN_URL = "https://example.com/game/join?gameId="; // 참여 URL 기본 값
    private final Long ROOM_TTL = 3600000L; // 방 TTL: 1시간
    private final MemberService memberService;

    /**
     * 새로운 방 생성, 방 번호 리턴, redis에 저장
     */
    public CreateGameRespDto createGame() {
        String gameId = UUID.randomUUID().toString();

        String key = REDIS_GAME_PREFIX + gameId;
        if (!redisUtil.hasKey(key)) {
            // 빈 HashMap으로 방 생성 (TTL: 1시간)
            redisUtil.setValue(key, new HashMap<>(), ROOM_TTL);
        }

        // 참여 URL 생성 (예: https://example.com/game/join?gameId=생성된ID)
        String joinUrl = BASE_JOIN_URL + gameId;

        // 응답 DTO를 빌더 패턴을 이용해 생성하여 반환
        return CreateGameRespDto.builder()
                .gameId(gameId)
                .joinUrl(joinUrl)
                .message("Game created successfully")
                .build();
    }

    public void deleteGame(DeleteGameReqDto gameId) {
        String key = REDIS_GAME_PREFIX + gameId;
        if (redisUtil.hasKey(key)) {

            Object value = redisUtil.getValue(key);
            Map<String, Object> players;

            if (value instanceof Map) {
                players = (Map<String, Object>) value;
            } else {
                players = new HashMap<>();
            }
            
            // // 게임 결과 계산 (여기서는 예시로 간단한 결과 객체를 생성)
            // GameResult gameResult = computeGameResult(gameId, players);
            
            // // 각 참여자에 대해 멤버 도메인에 게임 결과 저장
            // for (String playerIdStr : players.keySet()) {
            //     Long playerId = Long.valueOf(playerIdStr);
            //     memberService.saveGameResult(playerId, gameResult);
            // }
            
            // // Redis에서 게임 방 삭제
            // redisUtil.deleteValue(key);
        }
    }

    public JoinGameRespDto joinGame(String gameId, JoinGameReqDto request) {
        String key = REDIS_GAME_PREFIX + gameId;
        if (!redisUtil.hasKey(key)) {
            throw new IllegalArgumentException("Game not found");
        }
        // Redis에 저장된 값은 플레이어 정보를 담은 Map<String, Object>로 가정
        Object value = redisUtil.getValue(key);
        Map<String, Object> players;

        if (value instanceof Map) {
            players = (Map<String, Object>) value;
        } else {
            players = new HashMap<>();
        }
        // playerId를 문자열로 변환하여 key로 사용하고, 값으로도 저장
        String participantKey = String.valueOf(request.getPlayerId());
        players.put(participantKey, request.getPlayerId());
        // 업데이트된 플레이어 목록을 Redis에 다시 저장하면서 TTL 갱신
        redisUtil.setValue(key, players, ROOM_TTL);

        return JoinGameRespDto.builder()
                .gameId(gameId)
                .message("Joined game successfully with playerId " + request.getPlayerId())
                .build();
    }

    public void removePlayer(DeleteGameReqDto gameId, Long playerId) {
        String key = REDIS_GAME_PREFIX + gameId;
        if (redisUtil.hasKey(key)) {
            HashMap<String, Object> game = (HashMap<String, Object>) redisUtil.getValue(key);
            game.remove(playerId.toString());
            redisUtil.setValue(key, game, 3600000L);
        }
    }


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
