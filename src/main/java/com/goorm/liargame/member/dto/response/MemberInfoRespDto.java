package com.goorm.liargame.member.dto.response;

import com.goorm.liargame.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberInfoRespDto {

    private String username;
    private String profileImageUrl;
    private String email;

    @Builder
    public MemberInfoRespDto(String username, String profileImageUrl, String email) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
    }

    public static MemberInfoRespDto from(Member member) {
        return MemberInfoRespDto.builder()
                .username(member.getUsername())
                .profileImageUrl(member.getProfileImageUrl())
                .email(member.getEmail())
                .build();
    }
}
