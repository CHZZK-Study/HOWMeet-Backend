package org.chzzk.howmeet.domain.temporary.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;

import static org.chzzk.howmeet.domain.temporary.auth.entity.QGuest.guest;

@RequiredArgsConstructor
public class GuestQueryDSLImpl implements GuestQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByGuestId(final Long guestId) {
        final Integer fetchFirst = queryFactory.selectOne()
                .from(guest)
                .where(guest.id.eq(guestId))
                .fetchFirst();

        return fetchFirst != null;
    }

    @Override
    public boolean existsByGuestScheduleIdAndNickname(final Long guestScheduleId, final Nickname nickname) {
        final Integer fetchFirst = queryFactory.selectOne()
                .from(guest)
                .where(guest.guestScheduleId.eq(guestScheduleId).and(guest.nickname.eq(nickname)))
                .fetchFirst();

        return fetchFirst != null;
    }
}
