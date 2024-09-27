package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;

public record MSCreateResponse(Long roomId) {
    public static MSCreateResponse from(final Room room) {
        return new MSCreateResponse(
                room.getId()
        );
    }
}
