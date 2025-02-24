package com.goorm.liargame.member.repository;

import com.goorm.liargame.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialId(String socialId);
    Optional<Member> findByEmail(String email);
}
