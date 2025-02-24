package com.goorm.liargame.member.application;

import com.goorm.liargame.auth.jwt.utils.JwtProperties;
import com.goorm.liargame.global.common.utils.RedisUtil;
import com.goorm.liargame.member.dto.request.LogoutReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final RedisUtil redisUtil;
    private final JwtProperties jwtProperties;

    public void logout(LogoutReqDto request) {
        redisUtil.setValue(request.getToken(), "blacklist:" + request.getUsername(), jwtProperties.getAccessExpirationTime());
    }
}
