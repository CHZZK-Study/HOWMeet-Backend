package org.chzzk.howmeet.domain.temporary.schedule.repository;

import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GSRepository extends JpaRepository<GuestSchedule, Long>, GSQueryDSL {
    @Query("SELECT gs.id FROM GuestSchedule gs WHERE gs.status = :status AND gs.createdAt < :cutoffDate")
    List<Long> findIdsByStatusAndCreatedAtBefore(@Param("status") ScheduleStatus status, @Param("cutoffDate") LocalDateTime cutoffDate);
    @Query("SELECT gs.id FROM GuestSchedule gs WHERE gs.status = :status AND gs.updatedAt < :cutoffDate")
    List<Long> findIdsByStatusAndUpdatedAtBefore(@Param("status") ScheduleStatus status, @Param("cutoffDate") LocalDateTime cutoffDate);

    @Query("SELECT new org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse(gs.id, gs.dates, gs.time, gs.name, gs.status) " +
            "FROM GuestSchedule gs WHERE gs.id = :guestScheduleId")
    Optional<GSResponse> findGuestScheduleDtoById(@Param("guestScheduleId") Long guestScheduleId);
}