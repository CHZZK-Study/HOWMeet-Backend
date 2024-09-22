package org.chzzk.howmeet.domain.regular.room.repository;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r " +
            "LEFT JOIN FETCH r.members rm " +
            "LEFT JOIN Member m ON m.id = rm.memberId " +
            "WHERE r.id = :roomId")
    Optional<Room> findRoomWithMembersAndNicknames(@Param("roomId") Long roomId);
}