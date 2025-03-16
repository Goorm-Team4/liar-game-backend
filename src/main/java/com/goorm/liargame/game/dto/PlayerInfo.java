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

    private String profileImg;
    private final static String NICKNAME_KEY = "nickname";
    private final static String PROFILE_IMG_KEY = "profileImg";

    @Builder
    public PlayerInfo(Long playerId, String nickname, String profileImg) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public static PlayerInfo from(Map.Entry<Long, Map<String, String>> player) {
        return PlayerInfo.builder()
                .playerId(player.getKey())

                .nickname(player.getValue().get(NICKNAME_KEY))
                .profileImg(player.getValue().get(PROFILE_IMG_KEY))
                .build();
    }

    public static PlayerInfo from(Long playerId, Map<String, String> player) {
        return PlayerInfo.builder()
                .playerId(playerId)

                .nickname(player.get(NICKNAME_KEY))
                .profileImg(player.get(PROFILE_IMG_KEY))
                .build();
    }
}