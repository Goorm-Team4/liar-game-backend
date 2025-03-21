package com.goorm.liargame.member.presentation;

import com.goorm.liargame.auth.domain.CustomUserDetails;
import com.goorm.liargame.global.common.dto.ApiResponse;
import com.goorm.liargame.member.application.MemberService;
import com.goorm.liargame.member.dto.request.UpdateInfoReqDto;
import com.goorm.liargame.member.dto.response.MemberInfoRespDto;
import com.goorm.liargame.member.success.MemberSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "회원", description = "회원 정보 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberInfoRespDto>> getMemberInfo(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success(MemberSuccessCode.GET_MEMBER_SUCCESS,
                memberService.getMemberInfo(user.getEmail())));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @AuthenticationPrincipal CustomUserDetails user) {
        memberService.deleteMember(user.getEmail());

        return ResponseEntity.ok(ApiResponse.success(MemberSuccessCode.DELETE_MEMBER_SUCCESS));
    }

    @Operation(summary = "회원 정보 수정")
    @PatchMapping(value = "/me", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<MemberInfoRespDto>> updateMemberInfo(
            @RequestPart(value = "image", required = false) MultipartFile profileImage,
            @RequestPart(required = false) UpdateInfoReqDto request,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(ApiResponse.success(MemberSuccessCode.UPDATE_MEMBER_SUCCESS,
                memberService.updateMemberInfo(request, profileImage, user.getEmail())));
    }
}
