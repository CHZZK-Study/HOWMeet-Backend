package org.chzzk.howmeet.domain.temporary.schedule.repository;

import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GSRepository extends JpaRepository<GuestSchedule, Long>, GSQueryDSL {
    List<GuestSchedule> findByStatusAndCreatedAtBefore(ScheduleStatus status, LocalDateTime cutoffDate);
    List<GuestSchedule> findByStatusAndUpdatedAtBefore(ScheduleStatus status, LocalDateTime cutoffDate);
}