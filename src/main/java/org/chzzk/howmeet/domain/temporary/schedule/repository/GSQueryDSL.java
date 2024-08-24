package org.chzzk.howmeet.domain.temporary.schedule.repository;

public interface GSQueryDSL {
    boolean existsByGuestScheduleId(final Long guestScheduleId);
}
