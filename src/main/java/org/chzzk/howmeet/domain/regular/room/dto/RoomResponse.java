package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;

public record RoomResponse(Long roomId, String name, String description) {
    public static RoomResponse of(final Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName().getValue(),
                room.getDescription().getValue()
        );
    }
}
