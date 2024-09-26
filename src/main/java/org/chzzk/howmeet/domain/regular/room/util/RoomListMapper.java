package org.chzzk.howmeet.domain.regular.room.util;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.room.dto.RoomListResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.dto.CompletedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.ProgressedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RoomListMapper {

    public static RoomListResponse toRoomListResponse(final Room room, final List<MemberSchedule> memberSchedules, String leaderNickname, ConfirmScheduleFinder confirmScheduleFinder) {
        int totalMembers = room.getMembers().size();

        String memberSummary = String.format("%s 외 %d명", leaderNickname, totalMembers - 1);

        List<MSResponse> schedules = memberSchedules.stream()
                .sorted(Comparator.comparing(BaseEntity::getCreatedAt))
                .map(memberSchedule -> {
                    if (memberSchedule.getStatus() == ScheduleStatus.COMPLETE) {
                        ConfirmSchedule confirmSchedule = confirmScheduleFinder.findConfirmScheduleByMSId(memberSchedule.getId());
                        List<LocalDateTime> confirmTimes = confirmSchedule.getTimes();
                        List<String> dates = extractDatesFromConfirmTimes(confirmTimes);
                        ScheduleTime scheduleTime = extractScheduleTimeFromConfirmTimes(confirmTimes);
                        return CompletedMSResponse.of(memberSchedule, dates, scheduleTime);
                    } else {
                        return ProgressedMSResponse.from(memberSchedule);
                    }
                })
                .collect(Collectors.toList());

        return new RoomListResponse(
                room.getId(),
                room.getName().getValue(),
                memberSummary,
                schedules
        );
    }

    public interface ConfirmScheduleFinder {
        ConfirmSchedule findConfirmScheduleByMSId(Long memberScheduleId);
    }

    private static List<String> extractDatesFromConfirmTimes(List<LocalDateTime> confirmTimes) {
        String date = confirmTimes.get(0).toLocalDate().toString();
        return List.of(date);
    }

    private static ScheduleTime extractScheduleTimeFromConfirmTimes(List<LocalDateTime> confirmTimes) {
        LocalTime startTime = confirmTimes.get(0).toLocalTime();
        LocalTime endTime = confirmTimes.get(1).toLocalTime();

        return ScheduleTime.of(startTime, endTime);
    }
}
