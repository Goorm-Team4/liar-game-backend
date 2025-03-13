package com.goorm.liargame.game.application;

import com.goorm.liargame.game.dto.request.CreateGameReqDto;
import com.goorm.liargame.game.dto.request.DeleteGameReqDto;
import com.goorm.liargame.game.dto.request.JoinGameReqDto;
import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.request.StartGameReqDto;
import com.goorm.liargame.game.dto.response.ChatMessageRespDto;
import com.goorm.liargame.game.dto.response.CreateGameRespDto;
import com.goorm.liargame.game.dto.response.JoinGameRespDto;
import com.goorm.liargame.game.dto.response.LiarAnswerRespDto;
import com.goorm.liargame.game.dto.response.StartGameRespDto;
import com.goorm.liargame.game.dto.response.TurnMessageRespDto;
import com.goorm.liargame.game.enums.Player;
import com.goorm.liargame.game.enums.Status;
import com.goorm.liargame.global.common.utils.RedisUtil;
import com.goorm.liargame.player.enums.Adjective;
import com.goorm.liargame.player.enums.CharacterType;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameService {

    private final RedisUtil redisUtil;
    private final String REDIS_GAME_PREFIX = "game:";
    private final Random random = new Random();


    public String generateUniqueNickname(String gameId) {
        String key = REDIS_GAME_PREFIX + gameId;
        String hashKey = "nicknames";
        String nickname;

        // Redis에서 해당 게임 방에 이미 사용된 닉네임 Set을 가져옵니다.
        // 만약 없으면 새로운 Set으로 간주합니다.
        @SuppressWarnings("unchecked")

        Set<String> usedNicknames = (Set<String>) redisUtil.getHashValue(key, hashKey);
        if (usedNicknames == null) {
            usedNicknames = new java.util.HashSet<>();
        }
        
        Set<String> usedAnimals = usedNicknames.stream()
                .map(nickName -> {
                    String[] parts = nickName.split(" ");
                    return parts.length >= 2 ? parts[1] : "";
                })
                .filter(animal -> !animal.isEmpty())
                .collect(Collectors.toSet());
                
        var availableAnimals = Arrays.stream(CharacterType.values())
                .filter(ct -> !usedAnimals.contains(ct.getName()))
                .collect(Collectors.toList());

        CharacterType chosenAnimal = availableAnimals.get(random.nextInt(availableAnimals.size()));
        Adjective chosenAdjective = Adjective.values()[random.nextInt(Adjective.values().length)];
        
        nickname = chosenAdjective.getName() + " " + chosenAnimal.getName();

        return nickname;
    }

    public CreateGameRespDto createGame(CreateGameReqDto request) {
        String gameId = UUID.randomUUID().toString();
        String status = "status";
        String players = "players";
        String key = REDIS_GAME_PREFIX + gameId;
        if (!redisUtil.hasKey(key)) {
            // 빈 HashMap으로 방 생성
            redisUtil.setPermanentValue(key, new HashMap<>());
        }


        // 게임 진행 상태 코드 저장
        redisUtil.setHashValue(key, status, Status.WAITING);
        // 게임 참여자 목록 저장
        Set<Long> playerList = new HashSet<>();
        redisUtil.setHashValue(key, players, playerList);

        
        // 응답 DTO를 빌더 패턴을 이용해 생성하여 반환
        return CreateGameRespDto.builder()
                .gameId(gameId) 
                .message("Game created successfully")
                .build();
    }

    public void deleteGame(DeleteGameReqDto gameId) {

    }

    public JoinGameRespDto joinGame(JoinGameReqDto request) {
        String key = REDIS_GAME_PREFIX + request.getGameId();


        if (!redisUtil.hasKey(key)) {
            return JoinGameRespDto.builder()
                    .message("Game not found")
                    .build();
        }
        
        Long playerId = request.getPlayerId();
        // 게임 방에 참여자 추가
        redisUtil.updateHashSetValue(key, "players", playerId);
        String nickname = generateUniqueNickname(request.getGameId());
        redisUtil.updateHashListValue(key, "nicknames", nickname);
        Map<String, String> nicknamesMap = new HashMap<>();
        nicknamesMap.put(String.valueOf(playerId), nickname);
        

        return JoinGameRespDto.builder()
                .message("Joined game successfully with playerId " + playerId)
                .build();
    }

    public StartGameRespDto startGame(StartGameReqDto request) {
        String key = REDIS_GAME_PREFIX + request.getGameId();
        String status = "status";
        String players = "players";
        String host = "hostId";
        

        if (!redisUtil.hasKey(key)) {
            return StartGameRespDto.builder()
                    .message("Game not found")
                    .build();
        }

        // 게임 방의 상태를 게임 중으로 변경
        redisUtil.setHashValue(key, status, Status.PLAYING);

        // 게임 참여자 목록을 가져옴
        @SuppressWarnings("unchecked")
        Set<Long> playerList = (Set<Long>) redisUtil.getHashValue(key, players);

        // 게임 시작 응답 DTO를 빌더 패턴을 이용해 생성하여 반환
        return StartGameRespDto.builder()
                .message("Game started successfully")
                // .playerList(new ArrayList<>(playerList))
                // .hostId((Long) redisUtil.getHashValue(key, host))
                .build();
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
