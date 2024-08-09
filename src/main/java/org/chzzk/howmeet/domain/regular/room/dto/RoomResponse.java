package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

import java.util.List;

public record RoomResponse(Long roomId, String name, String description, List<RoomMemberResponse> roomMembers, List<MemberSchedule> memberSchedules) {
    public static RoomResponse of(final Room room, final List<RoomMember> roomMembers, final List<MemberSchedule> memberSchedules) {
        List<RoomMemberResponse> roomMemberResponse = roomMembers.stream()
                .map(RoomMemberResponse::of)
                .toList();

        return new RoomResponse(
                room.getId(),
                room.getName().getValue(),
                room.getDescription().getValue(),
                roomMemberResponse,
                memberSchedules
        );
    }
}
