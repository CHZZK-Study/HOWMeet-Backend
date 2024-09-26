package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;

import java.util.List;

public record RoomResponse(Long roomId,
                           String name,
                           List<RoomMemberResponse> roomMembers,
                           List<MSResponse> schedules) {

    public static RoomResponse of(final Room room, final List<RoomMemberResponse> roomMemberResponse, final List<MSResponse> schedules) {
        return new RoomResponse(
                room.getId(),
                room.getName().getValue(),
                roomMemberResponse,
                schedules
        );
    }
}
