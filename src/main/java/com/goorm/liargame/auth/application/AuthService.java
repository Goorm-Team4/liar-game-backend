package com.goorm.liargame.auth.application;

import com.goorm.liargame.auth.domain.CustomUserDetails;
import com.goorm.liargame.auth.handler.OAuth2LogoutHandler;
import com.goorm.liargame.auth.jwt.utils.JwtProperties;
import com.goorm.liargame.auth.jwt.utils.JwtUtil;
import com.goorm.liargame.global.common.utils.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final OAuth2LogoutHandler oAuth2LogoutHandler;
    private final RedisUtil redisUtil;

    public void kakaoLogout(CustomUserDetails user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = jwtUtil.extractToken(request);
        if (token != null && user != null) {
            logout(user.getUsername(), token);
        }
        oAuth2LogoutHandler.redirectToKakaoLogout(response);
    }

    public void logout(String username, String token) {
        redisUtil.setValue(token, "blacklist:" + username, jwtProperties.getAccessExpirationTime());
    }
}
