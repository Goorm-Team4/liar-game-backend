package com.goorm.liargame.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Word {
    CAT("고양이", Topic.ANIMAL),
    DOG("강아지", Topic.ANIMAL)
    ;

    private final String name;
    private final Topic topic;
}

