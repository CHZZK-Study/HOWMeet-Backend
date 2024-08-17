package org.chzzk.howmeet.domain.temporary.record.repository;

import java.util.List;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TmpGuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByGuestScheduleId(@Param("guest_schedule_id") final Long guestScheduleId);

}
