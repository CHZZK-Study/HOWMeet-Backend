package org.chzzk.howmeet.domain.temporary.record.repository;

import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmpGSRepository extends JpaRepository<GuestSchedule, Long> {
}