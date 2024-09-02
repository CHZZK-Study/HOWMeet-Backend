package org.chzzk.howmeet.domain.regular.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static org.chzzk.howmeet.domain.regular.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberQueryDSLImpl implements MemberQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByMemberId(final Long memberId) {
        final Integer fetchFirst = queryFactory.selectOne()
                .from(member)
                .where(member.id.eq(memberId))
                .fetchFirst();

        return fetchFirst != null;
    }
}
