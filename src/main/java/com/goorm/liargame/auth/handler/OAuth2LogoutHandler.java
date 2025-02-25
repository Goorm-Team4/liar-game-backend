package com.goorm.liargame.auth.handler;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LogoutHandler {

    @Value("${spring.security.oauth2.client.logout.redirect-url}")
    private String redirectUrl;

    @Value("${spring.security.oauth2.client.logout.front-redirect-url}")
    private String frontRedirectUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;


    public String getKakaoLogoutUrl() {
        return redirectUrl + "?client_id=" + kakaoClientId +
                "&logout_redirect_uri=" + frontRedirectUrl;
    }

    public void redirectToKakaoLogout(HttpServletResponse response) throws IOException {
       response.sendRedirect(getKakaoLogoutUrl());
    }
}