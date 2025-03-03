package com.goorm.liargame.member.application;

import com.goorm.liargame.global.image.service.AmazonS3Service;
import com.goorm.liargame.member.domain.Member;
import com.goorm.liargame.member.dto.request.UpdateInfoReqDto;
import com.goorm.liargame.member.dto.response.MemberInfoRespDto;
import com.goorm.liargame.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;
    private final AmazonS3Service amazonS3Service;

    public void deleteMember(String email) {
        Member member = memberQueryService.findByEmail(email);
        memberRepository.delete(member);
    }

    public MemberInfoRespDto updateMemberInfo(UpdateInfoReqDto request, MultipartFile profileImage, String email) {
        Member member = memberQueryService.findByEmail(email);

        if (request.getUsername() != null) {
            member.updateUsername(request.getUsername());
        }

        String imageUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            imageUrl = amazonS3Service.uploadImage(profileImage);
            if (imageUrl != null) {
                member.updateProfileImageUrl(imageUrl);
            }
        }

        return MemberInfoRespDto.from(member);
    }
}
