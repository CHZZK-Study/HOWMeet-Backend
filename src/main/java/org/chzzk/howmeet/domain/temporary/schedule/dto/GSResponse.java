package org.chzzk.howmeet.domain.temporary.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleDate;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record GSResponse(Long guestScheduleId, String name, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String inviteLink) {
    public static GSResponse of(final GuestSchedule guestSchedule, final String inviteLink) {
        List<String> scheduleDates = guestSchedule.getDates();
        ScheduleTime scheduleTime = guestSchedule.getTime();
        LocalDate startDate = LocalDate.parse(scheduleDates.get(0));
        LocalDate endDate = LocalDate.parse(scheduleDates.get(scheduleDates.size() - 1));

        return new GSResponse(
                guestSchedule.getId(),
                guestSchedule.getName().getValue(),
                startDate,
                scheduleTime.getStartTime(),
                endDate,
                scheduleTime.getEndTime(),
                inviteLink
        );
    }
}
