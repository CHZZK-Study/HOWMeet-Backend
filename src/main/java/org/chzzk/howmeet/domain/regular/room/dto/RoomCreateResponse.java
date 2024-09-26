package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;

import java.util.List;

public record RoomCreateResponse(Long roomId) {

    public static RoomCreateResponse from(final Room room) {
        return new RoomCreateResponse(room.getId());
    }
}
