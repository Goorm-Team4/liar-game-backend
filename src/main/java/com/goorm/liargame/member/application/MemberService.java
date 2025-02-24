package com.goorm.liargame.member.application;

import com.goorm.liargame.auth.jwt.utils.JwtProperties;
import com.goorm.liargame.global.common.utils.RedisUtil;
import com.goorm.liargame.member.domain.Member;
import com.goorm.liargame.member.dto.request.LogoutReqDto;
import com.goorm.liargame.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final RedisUtil redisUtil;
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;

    public void logout(LogoutReqDto request) {
        redisUtil.setValue(request.getToken(), "blacklist:" + request.getUsername(), jwtProperties.getAccessExpirationTime());
    }

    public void deleteMember(String email) {
        Member member = memberQueryService.findByEmail(email);
        memberRepository.delete(member);
    }
}
