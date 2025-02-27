package com.goorm.liargame.auth.presentation;

import com.goorm.liargame.auth.application.AuthService;
import com.goorm.liargame.auth.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "인증", description = "인증 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그아웃")
    @PostMapping("/logout/kakao")
    public void kakaoLogout(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.kakaoLogout(user, request, response);
    }
}
