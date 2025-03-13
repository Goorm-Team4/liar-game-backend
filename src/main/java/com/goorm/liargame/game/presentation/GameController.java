package com.goorm.liargame.game.presentation;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.goorm.liargame.game.dto.request.FinalVoteReqDto;
import com.goorm.liargame.game.dto.response.FinalVoteRespDto;
import com.goorm.liargame.game.application.GameService;
import com.goorm.liargame.game.dto.request.FinalVoteResultReqDto;
import com.goorm.liargame.game.dto.request.LiarAnswerReqDto;
import com.goorm.liargame.game.dto.request.MessageReqDto;
import com.goorm.liargame.game.dto.response.ChatMessageRespDto;
import com.goorm.liargame.game.dto.response.FinalVoteResultRespDto;
import com.goorm.liargame.game.dto.response.LiarAnswerRespDto;
import com.goorm.liargame.game.dto.response.TurnMessageRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

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
                                              @DestinationVariable String gameId) throws JsonProcessingException {
        return gameService.sendTurnMessage(gameId, request);
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

    /**
     * 최종 투표 전송하는 메서드
     */
    @MessageMapping("/games/{gameId}/final-vote")
    @SendTo("/sub/games/{gameId}/final-vote")
    public FinalVoteRespDto sendFinalVote(FinalVoteReqDto request,
                                          @DestinationVariable String gameId) {
        return gameService.sendFinalVote(gameId, request);
    }

    /**
     * 최종 투표 결과를 받고 전송하는 메서드
     */
    @MessageMapping("/games/{gameId}/final-vote/result")
    @SendTo("/sub/games/{gameId}/final-vote/result")
    public FinalVoteResultRespDto sendFinalVoteResult(FinalVoteResultReqDto request,
                                                         @DestinationVariable String gameId) {
        return gameService.sendFinalVoteResult(gameId, request);
    }

    /**
     * 라이어의 정답을 판별하여 전송하는 메서드
     */
    @MessageMapping("/games/{gameId}/answer")
    @SendTo("/sub/games/{gameId}/result")
    public LiarAnswerRespDto verifyLiarAnswer(LiarAnswerReqDto request,
                                              @DestinationVariable String gameId) {
        return gameService.verifyLiarAnswer(gameId, request);
    }

//    @MessageMapping("/games/{gameId}/turn")
//    @SendTo("/sub/games/{gameId}")
//    public TurnInfoRespDto sendTurnInfo(TurnInfoReqDto request,
//                                        @DestinationVariable String gameId) {
//        return gameService.sendTurnInfo(request);
//    }
}
