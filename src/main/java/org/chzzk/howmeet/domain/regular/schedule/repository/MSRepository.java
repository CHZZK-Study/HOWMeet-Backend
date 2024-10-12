package org.chzzk.howmeet.domain.regular.schedule.repository;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MSRepository extends JpaRepository<MemberSchedule, Long> {
    Optional<MemberSchedule> findByIdAndRoomId(Long msId, Long roomId);

    @Modifying
    @Transactional
    @Query("UPDATE MemberSchedule ms SET ms.disable = true WHERE ms.id = :msId AND ms.room.id = :roomId")
    void deactivateMemberSchedule(@Param("msId") Long msId, @Param("roomId") Long roomId);

    boolean existsByIdAndRoomId(Long msId, Long roomId);
}