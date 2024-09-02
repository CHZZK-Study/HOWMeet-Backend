package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record RoomListResponse (Long roomId, String name, String memberSummary, List<MSResponse> schedules) {

    public RoomListResponse(Long roomId, String name, String memberSummary, List<MSResponse> schedules) {
        this.roomId = roomId;
        this.name = name;
        this.memberSummary = memberSummary;
        this.schedules = schedules;
    }

    public static RoomListResponse of(final Room room, final List<MemberSchedule> memberSchedules, String leaderNickname) {
        int totalMembers = room.getMembers().size();

        Optional<RoomMember> leaderRoomMember = room.getMembers().stream()
                .filter(RoomMember::getIsLeader)
                .findFirst();

        String memberSummary = totalMembers > 1
                ? String.format("%s 외 %d명", leaderNickname, totalMembers - 1)
                : "1명";

        List<MSResponse> schedules = memberSchedules.stream()
                .filter(schedule -> schedule.getStatus() == ScheduleStatus.PROGRESS)
                .sorted(Comparator.comparing(BaseEntity::getCreatedAt))
                .map(MSResponse::from)
                .collect(Collectors.toList());

        return new RoomListResponse(
                room.getId(),
                room.getName().getValue(),
                memberSummary,
                schedules
        );
    }
}
