package com.goorm.liargame.auth.presentation;

import com.goorm.liargame.auth.application.AuthService;
import com.goorm.liargame.auth.domain.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/logout/kakao")
    public void kakaoLogout(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.kakaoLogout(user, request, response);
    }
}
