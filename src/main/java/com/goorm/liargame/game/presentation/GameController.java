package com.goorm.liargame.game.presentation;


import com.goorm.liargame.game.application.GameService;
import com.goorm.liargame.game.dto.request.JoinGameReqDto;
import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.response.ChatMessageRespDto;
import com.goorm.liargame.game.dto.response.CreateGameRespDto;
import com.goorm.liargame.game.dto.response.JoinGameRespDto;
import com.goorm.liargame.game.dto.response.LiarAnswerRespDto;
import com.goorm.liargame.game.dto.response.TurnMessageRespDto;
import lombok.RequiredArgsConstructor;

import org.hibernate.mapping.Join;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@Controller
public class GameController {

    private final GameService gameService;

    /**
     * 턴 채팅 받아서 전송하는 메서드
     */
    @MessageMapping("/games/{gameId}/chat/turn") // 메시지 보내는 주소 -> /pub/games/{gameId}/chat/turn
    @SendTo("/sub/games/{gameId}/chat/turn") // 메시지 받는 주소 -> /sub/games/{gameId}/chat/turn
    public TurnMessageRespDto sendTurnMessage(MessageReqDto request,
                                              @DestinationVariable String gameId) {
        return gameService.sendTurnMessage(request);
    }

    /**
     * 최후 변론 채팅 받아서 전송하는 메서드
     */
    @MessageMapping("/games/{gameId}/chat/final")
    @SendTo("/sub/games/{gameId}/chat/final")
    public ChatMessageRespDto sendChatMessage(MessageReqDto request,
                                              @DestinationVariable String gameId) {
        return gameService.sendChatMessage(request);
    }

//    /**
//     * 최종 투표 결과를 받아 라이어 여부를 전송하는 메서드
//     */
//    @MessageMapping("/games/{gameId}/final-vote")
//    @SendTo("/sub/games/{gameId}")
//    public FinalVoteResultRespDto processFinalVoteResult(FinalVoteResultReqDto request,
//                                                         @DestinationVariable String gameId) {
//        return gameService.processFinalVoteResult(request);
//    }

    /**
     * 라이어의 정답을 판별하여 전송하는 메서드
     */
    @MessageMapping("/games/{gameId}/answer")
    @SendTo("/sub/games/{gameId}/result")
    public LiarAnswerRespDto verifyLiarAnswer(LiarAnswerReqDto request,
                                              @DestinationVariable String gameId) {
        return gameService.verifyLiarAnswer(request);
    }

//    @MessageMapping("/games/{gameId}/turn")
//    @SendTo("/sub/games/{gameId}")
//    public TurnInfoRespDto sendTurnInfo(TurnInfoReqDto request,
//                                        @DestinationVariable String gameId) {
//        return gameService.sendTurnInfo(request);
//    }

    /**
     * 방 생성 API
     */
    @PostMapping("/games/create")
    public ResponseEntity<CreateGameRespDto> createGame() {
        CreateGameRespDto response = gameService.createGame();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/games/{gameId}/join")
    public ResponseEntity<JoinGameRespDto> joinGame(
        @PathVariable String gameId,
        @RequestBody JoinGameReqDto request){
        JoinGameRespDto response = gameService.joinGame(gameId, request);
        return ResponseEntity.ok(response);
    }

}
