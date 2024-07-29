package org.chzzk.howmeet.domain.regular.auth.repository;

import org.chzzk.howmeet.domain.regular.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
