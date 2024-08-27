package org.chzzk.howmeet.domain.temporary.schedule.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static org.chzzk.howmeet.domain.temporary.schedule.entity.QGuestSchedule.guestSchedule;

@RequiredArgsConstructor
public class GSQueryDSLImpl implements GSQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByGuestScheduleId(final Long guestScheduleId) {
        final Integer fetchFirst = queryFactory.selectOne()
                .from(guestSchedule)
                .where(guestSchedule.id.eq(guestScheduleId))
                .fetchFirst();

        return fetchFirst != null;
    }
}
