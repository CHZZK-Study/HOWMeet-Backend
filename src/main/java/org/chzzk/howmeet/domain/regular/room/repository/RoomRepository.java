package org.chzzk.howmeet.domain.regular.room.repository;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}