package com.goorm.liargame.member.domain;

import com.goorm.liargame.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static com.goorm.liargame.member.domain.Role.USER;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false, unique = true, length = 20)
    private String socialId;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    private String profileImageUrl = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = USER;

    @Column(nullable = false, unique = true, length = 50)
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

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateProfileImageUrl(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }
}
