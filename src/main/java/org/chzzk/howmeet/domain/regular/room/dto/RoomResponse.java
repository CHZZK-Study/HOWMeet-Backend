package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.util.List;
import java.util.stream.Collectors;

public record RoomResponse(Long roomId, String name, String description,
                           List<RoomMemberResponse> roomMembers,
                           List<MSResponse> ongoingSchedules,
                           List<MSResponse> completedSchedules) {

    public static RoomResponse of(final Room room, final List<RoomMember> roomMembers, final List<MemberSchedule> memberSchedules) {
        List<RoomMemberResponse> roomMemberResponse = roomMembers.stream()
                .map(RoomMemberResponse::from)
                .collect(Collectors.toList());

        List<MSResponse> ongoingSchedules = memberSchedules.stream()
                .filter(schedule -> schedule.getStatus() == ScheduleStatus.PROGRESS)
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt())) // 생성 순서대로 정렬
                .map(MSResponse::from)
                .collect(Collectors.toList());

        List<MSResponse> completedSchedules = memberSchedules.stream()
                .filter(schedule -> schedule.getStatus() == ScheduleStatus.COMPLETE)
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt())) // 생성 순서대로 정렬
                .map(MSResponse::from)
                .collect(Collectors.toList());

        return new RoomResponse(
                room.getId(),
                room.getName().getValue(),
                room.getDescription().getValue(),
                roomMemberResponse,
                ongoingSchedules,
                completedSchedules
        );
    }
}
