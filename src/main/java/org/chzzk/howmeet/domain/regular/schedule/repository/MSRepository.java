package org.chzzk.howmeet.domain.regular.schedule.repository;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.dto.CompletedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.ProgressedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MSRepository extends JpaRepository<MemberSchedule, Long> {
    Optional<MemberSchedule> findByIdAndRoomId(Long msId, Long roomId);

    @Query("SELECT ms.status FROM MemberSchedule ms WHERE ms.id = :msId AND ms.room.id = :roomId")
    Optional<ScheduleStatus> findStatusByIdAndRoomId(@Param("msId") Long msId, @Param("roomId") Long roomId);

    @Modifying
    @Transactional
    @Query("UPDATE MemberSchedule ms SET ms.disable = true WHERE ms.id = :msId AND ms.room.id = :roomId")
    void deactivateMemberSchedule(@Param("msId") Long msId, @Param("roomId") Long roomId);

    @Query("SELECT new org.chzzk.howmeet.domain.regular.schedule.dto.ProgressedMSResponse(ms.id, ms.dates, ms.time, ms.name, ms.status) " +
            "FROM MemberSchedule ms WHERE ms.id = :msId AND ms.room.id = :roomId AND ms.status = 'PROGRESS'")
    Optional<ProgressedMSResponse> findProgressScheduleDto(@Param("msId") Long msId, @Param("roomId") Long roomId);

    boolean existsByIdAndRoomId(Long msId, Long roomId);
}