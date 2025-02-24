package com.goorm.liargame.player.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Adjective {
    CUTE("귀여운"),
    ;

    private final String name;
}
