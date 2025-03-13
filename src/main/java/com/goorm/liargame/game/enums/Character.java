package com.goorm.liargame.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Character {
    DEAR("사슴", "deer"),
    ELER("코끼리", "elephant"),
    MUSHROOM("버섯", "mushroom"),
    PALM("야자수", "palm"),
    ROCK("바위", "rock"),
    SHARK("상어", "shark"),
    SNAIL("달팽이", "snail"),
    ;

    private String name;
    private String imageKey;
    private static final String BASE_IMAGE_URL = "https://liargame-bucket.s3.ap-northeast-2.amazonaws.com/characters/";

    public String getImageUrl() {
        return BASE_IMAGE_URL + imageKey + ".png";
    }
}
