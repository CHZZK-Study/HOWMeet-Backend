package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleDate;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record MSResponse(Long memberScheduleId, String name, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String inviteLink) {
    public static MSResponse of(final MemberSchedule memberSchedule, final String inviteLink) {
        ScheduleDate scheduleDate = memberSchedule.getDate();
        return new MSResponse(
                memberSchedule.getId(),
                memberSchedule.getName().getValue(),
                scheduleDate.getStartDate().toLocalDate(),
                scheduleDate.getStartDate().toLocalTime(),
                scheduleDate.getEndDate().toLocalDate(),
                scheduleDate.getEndDate().toLocalTime(),
                inviteLink
        );
    }
}
