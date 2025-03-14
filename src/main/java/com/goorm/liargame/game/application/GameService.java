package com.goorm.liargame.game.application;

import com.goorm.liargame.game.dto.request.CreateGameReqDto;
import com.goorm.liargame.game.dto.request.EndGameReqDto;
import com.goorm.liargame.game.dto.request.FinalVoteResultReqDto;
import com.goorm.liargame.game.dto.request.JoinGameReqDto;
import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.request.MidtermVoteReqDto;
import com.goorm.liargame.game.dto.request.StartGameReqDto;
import com.goorm.liargame.game.dto.response.ChatMessageRespDto;
import com.goorm.liargame.game.dto.response.CreateGameRespDto;
import com.goorm.liargame.game.dto.response.EndGameRespDto;
import com.goorm.liargame.game.dto.response.JoinGameRespDto;
import com.goorm.liargame.game.dto.response.LiarAnswerRespDto;
import com.goorm.liargame.game.dto.response.MidtermVoteRespDto;
import com.goorm.liargame.game.dto.response.MidtermVoteResultRespDto;
import com.goorm.liargame.game.dto.response.StartGameRespDto;
import com.goorm.liargame.game.dto.response.TurnMessageRespDto;
import com.goorm.liargame.game.enums.PlayerType;
import com.goorm.liargame.game.enums.Status;
import com.goorm.liargame.game.enums.Topic;
import com.goorm.liargame.game.enums.Word;
import com.goorm.liargame.global.common.utils.RedisUtil;
import com.goorm.liargame.game.enums.Adjective;
import com.goorm.liargame.game.enums.Character;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameService {

    private final RedisUtil redisUtil;
    private static Random random = new Random();
    public static String GAME_PREFIX = "game:";
    public static String PLAYERS_KEY = "players";
    public static String FINAL_VOTE_KEY = "final-vote";
    public static String MIDTERM_VOTE_KEY = "midterm-vote";
    public static String WORD_KEY = "word";
    public static String LIAR_KEY = "liar";
    public static String HOST_KEY = "host";
    public static String ORDER_KEY = "order";
    public static String STATUS_KEY = "status";
    public static String TURMTIME = "turn-time";
    public static String FINALTIME = "final-time";
    public static String NICKNAME = "nickname";
    public static String PROFILEIMG = "profileImg";
    public static String WINNER = "winner";

    public Character sampleUniqueAnimal(String GAME_KEY) {

        // Redis에서 해당 게임 방에 이미 사용된 닉네임 Set을 가져옵니다.
        // 만약 없으면 새로운 Set으로 간주합니다.
        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
        Map<Long, Object> players = (Map<Long, Object>) game.get(PLAYERS_KEY);

        Set<String> usedAnimals = players.values().stream()
        .map(player -> {
            // 플레이어 정보를 Map<String, String>로 캐스팅
            Map<String, String> playerData = (Map<String, String>) player;
            String nickname = playerData.get("nickname");
            if (nickname == null) return "";
            String[] parts = nickname.split(" ");
            return parts.length >= 2 ? parts[1] : "";
        })
        .filter(animal -> !animal.isEmpty())
        .collect(Collectors.toSet());
                
        // 사용되지 않은 동물 목록 필터링
        List<Character> availableAnimals = Arrays.stream(Character.values())
        .filter(ct -> !usedAnimals.contains(ct.getName()))
        .collect(Collectors.toList());

        // 사용 가능한 동물이 없다면, 모든 동물 중에서 선택하거나 예외처리
        if (availableAnimals.isEmpty()) {
            availableAnimals = Arrays.asList(Character.values());
        }

        // 무작위로 동물과 형용사를 선택하여 닉네임 생성
        Character chosenAnimal = availableAnimals.get(random.nextInt(availableAnimals.size()));
        
        return chosenAnimal;
    }

    public static Word getRandomWord() {
        Word[] words = Word.values();
        return words[random.nextInt(words.length)];
    }

    public CreateGameRespDto createGame() {
        String gameId = UUID.randomUUID().toString();
        String GAME_KEY = GAME_PREFIX + gameId;

        // 빈 HashMap으로 방 생성
        Map<String, Object> gameData = new HashMap<>();
        // 게임 진행 상태 코드 생성 및 저장
        gameData.put(STATUS_KEY, Status.WAITING);
        // 게임 참여자 목록 생성
        gameData.put(PLAYERS_KEY, new HashMap<>());
        // 게임 호스트 초기화
        gameData.put(HOST_KEY, null);
        // 최종 투표 결과 초기화
        gameData.put(FINAL_VOTE_KEY, null);
        // 중간 투표 결과 초기화
        gameData.put(MIDTERM_VOTE_KEY, new HashMap<>());
        // 라이어 초기화
        gameData.put(LIAR_KEY, null);
        // 게임 진행 순서 초기화
        gameData.put(ORDER_KEY, new ArrayList<>());
        // 게임 단어 초기화
        Word word = getRandomWord();
        gameData.put(WORD_KEY, word);
        // 턴 메세지 시간 초기화
        gameData.put(TURMTIME, 30);
        // 최후변론 시간 초기화
        gameData.put(FINALTIME, 60);
        // 승리자 정보 초기화
        gameData.put(WINNER, null);

        redisUtil.setPermanentValue(GAME_KEY, gameData);
        
        // 응답 DTO를 빌더 패턴을 이용해 생성하여 반환
        return CreateGameRespDto.builder()
                .gameId(gameId) 
                .message("Game created successfully")
                .build();
    }


    public JoinGameRespDto joinGame(JoinGameReqDto request) {
        String GAME_KEY = GAME_PREFIX + request.getGameId();
        String nickname;
        String profileImgUrl;

        if (!redisUtil.hasKey(GAME_KEY)) {
            return JoinGameRespDto.builder()
                    .message("Game not found")
                    .build();
        }
        
        Long playerId = request.getPlayerId();
        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
        Map<Long, Object> players = (Map<Long, Object>) game.get(PLAYERS_KEY);


        // 게임 방의 참여자 목록을 가져옴
        if (players.isEmpty() || Objects.isNull(players)) {
            // 처음 들어온 사용자를 게임 방의 hostId로 설정
            game.put(HOST_KEY, playerId);
        }
        // 플레이어 정보를 담을 HashMap 생성 (nickname과 profileimage 저장)
        Map<String, String> playerData = new HashMap<>();

        // 게임 방에 참여자 추가
        // 닉네임 enum에서 랜덤으로 선택
        Character chosenAnimal = sampleUniqueAnimal(GAME_KEY);
        Adjective chosenAdjective = Adjective.values()[random.nextInt(Adjective.values().length)];

        nickname = chosenAdjective.getName() + " " + chosenAnimal.getName();
        profileImgUrl = chosenAnimal.getImageUrl();

        playerData.put(NICKNAME, nickname);
        playerData.put(PROFILEIMG, profileImgUrl);

        players.put(playerId, playerData);
        game.put(PLAYERS_KEY, players);

        redisUtil.setPermanentValue(GAME_KEY, game);


        return JoinGameRespDto.builder()
                .message("Joined game successfully with playerId " + playerId)
                .gameId(request.getGameId())
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();
    }

    public StartGameRespDto startGame(StartGameReqDto request) {
        String GAME_KEY = GAME_PREFIX + request.getGameId();

        if (!redisUtil.hasKey(GAME_KEY)) {
            return StartGameRespDto.builder()
                    .message("Game not found")
                    .build();
        }

        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));

        // 게임 방의 상태를 게임 중으로 변경
        game.put(STATUS_KEY, Status.PLAYING);

        // 게임 참여자 목록을 가져옴
        Map<Long, Object> players = (Map<Long, Object>) game.get(PLAYERS_KEY);
        List<Long> playerIds = new ArrayList<>(players.keySet());

        // 라이어 선택
        Long liarId = playerIds.get(random.nextInt(playerIds.size()));
        game.put(LIAR_KEY, liarId);

        // 게임 참여자 목록을 무작위로 섞어서 게임 순서로 저장
        Collections.shuffle(playerIds);
        game.put(ORDER_KEY, playerIds);

        // 게임 방의 단어와 주제를 가져옴
        Word word = (Word) game.get(WORD_KEY);
        Topic topic = word.getTopic();

        // 게임 방 정보를 Redis에 저장
        redisUtil.setPermanentValue(GAME_KEY, game);

        // 라이어인 경우
        if (request.getPlayerId() == liarId) {
            return StartGameRespDto.builder()
                    .message("Game started successfully")
                    .gameId(request.getGameId())
                    .word(word.getName())
                    .topic(topic.getName())
                    .playerType(PlayerType.LIAR)
                    .build();
        }
        // 시민인 경우
        else {
            return StartGameRespDto.builder()
                    .message("Game started successfully")
                    .gameId(request.getGameId())
                    .word(word.getName())
                    .topic(topic.getName())
                    .playerType(PlayerType.NORMAL)
                    .build();
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
        PlayerType winner = correct ? PlayerType.LIAR : PlayerType.NORMAL;

        return new LiarAnswerRespDto(correct, winner);
    }    

    public MidtermVoteRespDto sendMidtermVote(MidtermVoteReqDto request) {
        String GAME_KEY = GAME_PREFIX + request.getGameId();
        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
        Map<Long, Long> midtermVote = (Map<Long, Long>) game.get(MIDTERM_VOTE_KEY);

        midtermVote.put(request.getPlayerId(), request.getVotePlayerId());
        game.put(MIDTERM_VOTE_KEY, midtermVote);
        redisUtil.setPermanentValue(GAME_KEY, game);


        return new MidtermVoteRespDto(request.getPlayerId(), request.getVotePlayerId());
    }

    public MidtermVoteResultRespDto sendMidtermVoteResult(MidtermVoteReqDto request) {
        String GAME_KEY = GAME_PREFIX + request.getGameId();
        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
        Map<Long, Long> midtermVote = (Map<Long, Long>) game.get(MIDTERM_VOTE_KEY);


         // 투표 데이터가 없으면 빈 결과를 반환
        if (midtermVote == null || midtermVote.isEmpty()) {
            return new MidtermVoteResultRespDto(new ArrayList<>());
        }

        // 후보별 표 수를 세기 위한 맵
        Map<Long, Integer> voteCount = new HashMap<>();
        for (Long voteCandidateId : midtermVote.values()) {
            voteCount.put(voteCandidateId, voteCount.getOrDefault(voteCandidateId, 0) + 1);
        }

        // 최고 득표수를 찾음
        int maxCount = 0;
        for (Integer count : voteCount.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }

        // 최고 득표수와 동일한 후보들을 winners 리스트에 담음
        List<Long> winners = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : voteCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                winners.add(entry.getKey());
            }
        }

        // 결과 DTO에 winners(배열 또는 리스트)를 담아서 반환
        return new MidtermVoteResultRespDto(winners);
    }

    public EndGameRespDto endGame(EndGameReqDto request) {
        String GAME_KEY = GAME_PREFIX + request.getGameId();
        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));



        return new EndGameRespDto();
    }


}
