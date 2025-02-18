package com.goorm.liargame.member.domain;

import jakarta.persistence.*;
import lombok.*;

import static com.goorm.liargame.member.domain.Role.USER;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends com.goorm.liargame.global.common.entity.BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Column(nullable = false)
    private String username;

    private String profileImageUrl = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = USER;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder
    public Member(SocialType socialType, String socialId, String username, String profileImageUrl, String email) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.username = username;
        // TODO: 기본 프로필 이미지 URL 설정
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl
            : "";
        this.email = email;
    }
}
