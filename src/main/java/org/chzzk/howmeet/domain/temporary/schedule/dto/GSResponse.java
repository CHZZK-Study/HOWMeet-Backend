package org.chzzk.howmeet.domain.temporary.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record GSResponse(Long id, List<String> dates, ScheduleTime time, ScheduleName name, ScheduleStatus status) {
    public static GSResponse of(final GuestSchedule guestSchedule) {
        return new GSResponse(
                guestSchedule.getId(),
                guestSchedule.getDates(),
                guestSchedule.getTime(),
                guestSchedule.getName(),
                guestSchedule.getStatus()
        );
    }
}
