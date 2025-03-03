package com.goorm.liargame.player.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CharacterType {
    SHARK("상어"),
    ;

    private final String name;
}
