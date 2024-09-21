package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;

public record RoomRequest(
        RoomName name,
        Long leaderMemberId
) {
    public Room toEntity() {
        return new Room(name);
    }
}