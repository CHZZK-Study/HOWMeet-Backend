package org.chzzk.howmeet.domain.regular.schedule.repository;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MSRepository extends JpaRepository<MemberSchedule, Long> {
    Optional<MemberSchedule> findByIdAndRoomId(Long msId, Long roomId);
}