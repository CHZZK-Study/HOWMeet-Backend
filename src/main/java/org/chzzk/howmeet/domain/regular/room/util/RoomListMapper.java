package org.chzzk.howmeet.domain.regular.room.util;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.room.dto.RoomListResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.dto.CompletedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.ProgressedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.ProgressedMSWithParticipationResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RoomListMapper {

    public static RoomListResponse toRoomListResponse(final Room room, final List<MemberSchedule> memberSchedules, String leaderNickname, Long memberId, ConfirmScheduleFinder confirmScheduleFinder, MemberParticipationChecker participationChecker) {
        int totalMembers = room.getMembers().size();

        String memberSummary = String.format("%s 외 %d명", leaderNickname, totalMembers - 1);

        List<MSResponse> schedules = memberSchedules.stream()
                .sorted(Comparator.comparing(BaseEntity::getCreatedAt))
                .map(memberSchedule -> {
                    boolean isParticipant = participationChecker.isMemberParticipant(memberSchedule.getId(), memberId);
                    if (memberSchedule.getStatus() == ScheduleStatus.COMPLETE) {
                        ConfirmSchedule confirmSchedule = confirmScheduleFinder.findConfirmScheduleByMSId(memberSchedule.getId());
                        List<LocalDateTime> confirmTimes = confirmSchedule.getTimes();
                        List<String> dates = extractDatesFromConfirmTimes(confirmTimes);
                        ScheduleTime scheduleTime = extractScheduleTimeFromConfirmTimes(confirmTimes);
                        return CompletedMSResponse.of(memberSchedule, dates, scheduleTime);
                    } else {
                        return ProgressedMSWithParticipationResponse.of(memberSchedule, isParticipant);
                    }
                })
                .collect(Collectors.toList());

        boolean hasNonParticipant = schedules.stream()
                .anyMatch(schedule -> {
                    if (schedule instanceof ProgressedMSWithParticipationResponse progressedMS) {
                        return !progressedMS.isParticipant();
                    }
                    return false;
                });

        return new RoomListResponse(
                room.getId(),
                room.getName().getValue(),
                memberSummary,
                schedules,
                hasNonParticipant
        );
    }

    public interface ConfirmScheduleFinder {
        ConfirmSchedule findConfirmScheduleByMSId(Long memberScheduleId);
    }

    public interface MemberParticipationChecker {
        boolean isMemberParticipant(Long memberScheduleId, Long memberId);
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
