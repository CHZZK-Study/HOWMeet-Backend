package org.chzzk.howmeet.domain.temporary.record.repository;

import java.util.List;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TmpGuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByGuestScheduleId(@Param("guestScheduleId") final Long guestScheduleId);

}
