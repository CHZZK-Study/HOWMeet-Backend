package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MSResponse(
        Long memberScheduleId,
        String name,
        LocalDate startDate,
        LocalTime startTime,
        LocalDate endDate,
        LocalTime endTime,
        String inviteLink,
        RoomResponse room
) {
    public static MSResponse of(final MemberSchedule memberSchedule, final String inviteLink) {
        List<String> scheduleDates = memberSchedule.getDates();
        ScheduleTime scheduleTime = memberSchedule.getTime();
        LocalDate startDate = LocalDate.parse(scheduleDates.get(0));
        LocalDate endDate = LocalDate.parse(scheduleDates.get(scheduleDates.size() - 1));

        // Room 정보를 RoomResponse로 변환
        Room room = memberSchedule.getRoom();
        RoomResponse roomResponse = RoomResponse.of(room, room.getMembers());

        return new MSResponse(
                memberSchedule.getId(),
                memberSchedule.getName().getValue(),
                startDate,
                scheduleTime.getStartTime(),
                endDate,
                scheduleTime.getEndTime(),
                inviteLink,
                roomResponse
        );
    }
}