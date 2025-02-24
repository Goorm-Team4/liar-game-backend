package com.goorm.liargame.player.domain;

import com.goorm.liargame.game.domain.Game;
import com.goorm.liargame.global.common.entity.BaseEntity;
import com.goorm.liargame.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Player extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Adjective adjective;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CharacterType characterType;

    private boolean liar;

    private boolean AI;
}