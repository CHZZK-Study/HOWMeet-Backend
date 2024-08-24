package org.chzzk.howmeet.domain.temporary.record.repository;

import java.util.List;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GSRecordRepository extends JpaRepository<GuestScheduleRecord, Long> {

    List<GuestScheduleRecord> findByGuestScheduleId(@Param("guestScheduleId") final Long gsId);

    List<GuestScheduleRecord> findByGuestId(@Param("guestId") final Long guestId);

    void deleteByGuestId(@Param("guestId") final Long guestId);

}

