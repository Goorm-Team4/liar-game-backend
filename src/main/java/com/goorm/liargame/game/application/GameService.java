package com.goorm.liargame.game.application;


import com.goorm.liargame.game.dto.response.*;
import com.goorm.liargame.game.dto.request.*;

import com.goorm.liargame.game.enums.PlayerType;
import com.goorm.liargame.game.enums.Status;
import com.goorm.liargame.game.enums.Topic;
import com.goorm.liargame.game.enums.Word;
import com.goorm.liargame.global.common.utils.RedisUtil;
import com.goorm.liargame.game.enums.Adjective;
import com.goorm.liargame.game.enums.Character;
import com.goorm.liargame.game.dto.PlayerInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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
        
        // String을 Long으로 변환
        Long playerId = Long.parseLong(request.getPlayerId());
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
                .playerId(playerId.toString())
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
        if (Long.parseLong(request.getPlayerId()) == liarId) {
            return StartGameRespDto.builder()
                    .message("Game started successfully")
                    .gameId(request.getGameId())
                    .word(word.getName())
                    .topic(topic.getName())
                    .playerType(PlayerType.LIAR.toString())
                    .build();
        }
        // 시민인 경우
        else {
            return StartGameRespDto.builder()
                    .message("Game started successfully")
                    .gameId(request.getGameId())
                    .word(word.getName())
                    .topic(topic.getName())
                    .playerType(PlayerType.NORMAL.toString())
                    .build();
        }
    }



    public TurnMessageRespDto sendTurnMessage(String gameId, MessageReqDto request) {
        Map<String, Object> game = getGame(gameId);

        Long nextPlayerId = null;
        boolean lastPlayer = false;

//        Map<String, Object> new_game = new HashMap<>();
//        new_game.put("word", "사과");
//        new_game.put("order", new ArrayList<>(List.of(1L, 2L, 3L, 4L)));
//        redisUtil.setValue(GAME_KEY, new_game);

        List<Long> order = getOrder(game);
        Map<Long, Map<String, String>> players = getPlayers(game);

        int currentPlayerIdx = order.indexOf(request.getPlayerId());
        if (currentPlayerIdx == order.size() - 1) {
            lastPlayer = true;
        } else {
            nextPlayerId = order.get(currentPlayerIdx + 1);
        }

        PlayerInfo player = PlayerInfo.from(request.getPlayerId(), players.get(request.getPlayerId()));
        PlayerInfo nextPlayer = PlayerInfo.from(nextPlayerId, players.get(nextPlayerId));

        return TurnMessageRespDto.builder()
                .player(player)
                .content(request.getContent())
                .nextPlayer(nextPlayer)
                .lastPlayer(lastPlayer)
                .build();
    }

    public ChatMessageRespDto sendChatMessage(String gameId, MessageReqDto request) {
        Map<String, Object> game = getGame(gameId);

        Map<Long, Map<String, String>> players = getPlayers(game);
        PlayerInfo player = PlayerInfo.from(request.getPlayerId(), players.get(request.getPlayerId()));
        return new ChatMessageRespDto(player, request.getContent());
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



    public MidtermVoteRespDto sendMidtermVote(MidtermVoteReqDto request) {
        String GAME_KEY = GAME_PREFIX + request.getGameId();
        Map<String, Object> game = ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
        Map<Long, Long> midtermVote = (Map<Long, Long>) game.get(MIDTERM_VOTE_KEY);

        midtermVote.put(Long.parseLong(request.getPlayerId()), Long.parseLong(request.getPlayerId()));
        game.put(MIDTERM_VOTE_KEY, midtermVote);
        redisUtil.setPermanentValue(GAME_KEY, game);


        return new MidtermVoteRespDto(request.getPlayerId(), request.getPlayerId());
    }
      
    public LiarAnswerRespDto verifyLiarAnswer(String gameId, LiarAnswerReqDto request) {
        Map<String, Object> game = getGame(gameId);

        boolean correct = false;
        String word = getWord(game);

        if (word.equals(request.getAnswer())) correct = true;

        PlayerType winner = correct ? PlayerType.LIAR : PlayerType.NORMAL;

        Long liarId = (Long) getLiarId(game);
        Map<Long, Map<String, String>> players = getPlayers(game);
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
        Map<String, Object> game = getGame(gameId);
        Map<Long, Boolean> finalVote = getFinalVote(game);
        finalVote.put(request.getVoterId(), request.isKill());
        game.put(FINAL_VOTE_KEY, finalVote);
        Map<Long, Map<String, String>> players = getPlayers(game);
        PlayerInfo voter = PlayerInfo.from(request.getVoterId(), players.get(request.getVoterId()));

        redisUtil.setValue(GAME_PREFIX + gameId, game);

        return new FinalVoteRespDto(voter, request.isKill());
    }

    public FinalVoteResultRespDto sendFinalVoteResult(String gameId, FinalVoteResultReqDto request) {
        Map<String, Object> game = getGame(gameId);
//        Map<Long, Map<String, String>> aplayers = new HashMap<>();
//        Map<String, String> info = new HashMap<>();
//        info.put("nickname", "test");
//        info.put("profileImg", "test");
//        aplayers.put(1L, info);
//        aplayers.put(2L, info);
//        aplayers.put(3L, info);
//        aplayers.put(4L, info);
//        aplayers.put(5L, info);
//        game.put("liar", 1L);
//        game.put("players", aplayers);
//        redisUtil.setValue(GAME_KEY, game);
        Map<Long, Boolean> finalVote = getFinalVote(game);
        Map<Long, Map<String, String>> players = getPlayers(game);

        int size = players.size();
        long killCount = finalVote.values().stream().filter(v -> v).count();

        Long liarId = (Long) getLiarId(game);
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


    private static String getWord(Map<String, Object> game) {
        return (String) game.get(WORD_KEY);
    }

    private Map<String, Object> getGame(String gameId) {
        String GAME_KEY = GAME_PREFIX + gameId;
        return ((Map<String, Object>) redisUtil.getValue(GAME_KEY));
    }

    private static List<Long> getOrder(Map<String, Object> game) {
        return (List<Long>) game.get(ORDER_KEY);
    }

    private static Map<Long, Map<String, String>> getPlayers(Map<String, Object> game) {
        Map<String, Map<String, String>> rawPlayers = (Map<String, Map<String, String>>) game.get(PLAYERS_KEY);
        Map<Long, Map<String, String>> players = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : rawPlayers.entrySet()) {
            players.put(Long.parseLong(entry.getKey()), entry.getValue());
        }
        return players;
    }

    private static Map<Long, Boolean> getFinalVote(Map<String, Object> game) {
        return (Map<Long, Boolean>) game.get(FINAL_VOTE_KEY);
    }

    private static Object getLiarId(Map<String, Object> game) {
        return game.get(LIAR_KEY);
    }

    public MidtermVoteResultRespDto sendMidtermVoteResult(MidtermVoteResultReqDto request) {
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
        List<String> winners = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : voteCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                winners.add(entry.getKey().toString());
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
