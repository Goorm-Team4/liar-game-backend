package com.goorm.liargame.game.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerInfo {

    private Long playerId;
    private String nickname;
    private String profileUrl;

    @Builder
    public PlayerInfo(Long playerId, String nickname, String profileUrl) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public static PlayerInfo from(Map.Entry<Long, Map<String, String>> player) {
        return PlayerInfo.builder()
                .playerId(player.getKey())
                .nickname(player.getValue().get("nickname"))
                .profileUrl(player.getValue().get("profileUrl"))
                .build();
    }

    public static PlayerInfo from(Long playerId, Map<String, String> player) {
        log.info(player.toString());
        return PlayerInfo.builder()
                .playerId(playerId)
                .nickname(player.get("nickname"))
                .profileUrl(player.get("profileUrl"))
                .build();
    }
}