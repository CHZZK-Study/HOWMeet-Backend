package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.util.List;
import java.util.stream.Collectors;

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
