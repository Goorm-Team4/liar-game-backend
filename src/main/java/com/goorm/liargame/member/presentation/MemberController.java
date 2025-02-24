package com.goorm.liargame.member.presentation;

import com.goorm.liargame.auth.domain.CustomUserDetails;
import com.goorm.liargame.auth.jwt.utils.JwtUtil;
import com.goorm.liargame.global.common.dto.ApiResponse;
import com.goorm.liargame.member.application.MemberService;
import com.goorm.liargame.member.dto.request.LogoutReqDto;
import com.goorm.liargame.member.dto.request.UpdateInfoReqDto;
import com.goorm.liargame.member.dto.response.MemberInfoRespDto;
import com.goorm.liargame.member.success.MemberSuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        memberService.logout(new LogoutReqDto(user.getUsername(), token));

        return ResponseEntity.ok(ApiResponse.success(MemberSuccessCode.LOGOUT_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @AuthenticationPrincipal CustomUserDetails user) {
        memberService.deleteMember(user.getEmail());

        return ResponseEntity.ok(ApiResponse.success(MemberSuccessCode.DELETE_MEMBER_SUCCESS));
    }

    @PatchMapping(value = "/me", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<MemberInfoRespDto>> updateMemberInfo(
            @RequestPart(value = "image", required = false) MultipartFile profileImage,
            @RequestPart(required = false) UpdateInfoReqDto request,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(ApiResponse.success(MemberSuccessCode.UPDATE_MEMBER_SUCCESS,
                memberService.updateMemberInfo(request, profileImage, user.getEmail())));
    }
}
