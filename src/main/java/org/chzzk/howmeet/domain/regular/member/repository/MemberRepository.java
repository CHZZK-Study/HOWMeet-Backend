package org.chzzk.howmeet.domain.regular.member.repository;

import org.chzzk.howmeet.domain.regular.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(final String socialId);
}
