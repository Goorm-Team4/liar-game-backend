package com.goorm.liargame.member.application;

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

    public void deleteMember(String email) {
        Member member = memberQueryService.findByEmail(email);
        memberRepository.delete(member);
    }

    public MemberInfoRespDto updateMemberInfo(UpdateInfoReqDto request, MultipartFile profileImage, String email) {
        Member member = memberQueryService.findByEmail(email);

        if (request.getUsername() != null) {
            member.updateUsername(request.getUsername());
        }

        // TODO: S3 이미지 업로드 + 프로필 이미지 업데이트 기능 추가

        return MemberInfoRespDto.from(member);
    }
}
