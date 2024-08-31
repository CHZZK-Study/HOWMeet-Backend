package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.util.List;
import java.util.stream.Collectors;

public class RoomListResponse {

    private final Long roomId;
    private final String name;
    private final String memberCount;
    private final List<MSResponse> ongoingSchedules;

    // 생성자
    public RoomListResponse(Long roomId, String name, String memberCount, List<MSResponse> ongoingSchedules) {
        this.roomId = roomId;
        this.name = name;
        this.memberCount = memberCount;
        this.ongoingSchedules = ongoingSchedules;
    }

    // of 메서드
    public static RoomListResponse of(final Room room, final List<MemberSchedule> memberSchedules) {
        // 방에 포함된 인원 수 계산
        int totalMembers = room.getMembers().size();

        String memberCount = totalMembers > 1
                ? String.format("%d 외 %d명", totalMembers - 1, totalMembers - 1)
                : "1명";

        // 예정된 일정 리스트 가져오기
        List<MSResponse> ongoingSchedules = memberSchedules.stream()
                .filter(schedule -> schedule.getStatus() == ScheduleStatus.PROGRESS)
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .map(MSResponse::from)
                .collect(Collectors.toList());

        return new RoomListResponse(
                room.getId(),
                room.getName().getValue(),
                memberCount,
                ongoingSchedules
        );
    }
}
