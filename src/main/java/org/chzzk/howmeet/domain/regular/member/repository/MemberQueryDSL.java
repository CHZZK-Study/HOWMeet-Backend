package org.chzzk.howmeet.domain.regular.member.repository;

import org.springframework.data.repository.query.Param;

public interface MemberQueryDSL {
    boolean existsByMemberId(@Param("memberId") final Long memberId);
}
