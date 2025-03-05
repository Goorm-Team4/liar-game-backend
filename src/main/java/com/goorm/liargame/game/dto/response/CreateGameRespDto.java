package com.goorm.liargame.game.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateGameRespDto {
    // 생성된 게임 방의 고유 식별자
    private String gameId;
    // 클라이언트가 이 URL을 통해 방에 참여할 수 있습니다.
    private String joinUrl;
    // 방 생성 결과에 대한 메시지 (예: "Game created successfully")
    private String message;
}
