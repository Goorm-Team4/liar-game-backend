package com.goorm.liargame.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Topic {
    ANIMAL("동물"),
    ;

    private String name;
}
