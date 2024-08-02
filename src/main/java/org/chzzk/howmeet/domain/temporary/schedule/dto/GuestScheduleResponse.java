package org.chzzk.howmeet.domain.temporary.schedule.dto;

import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record GuestScheduleResponse(Long guestScheduleId, String name, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String inviteLink) {
    public static GuestScheduleResponse of(final GuestSchedule guestSchedule, final String inviteLink) {
        return new GuestScheduleResponse(
                guestSchedule.getId(),
                guestSchedule.getName().getValue(),
                guestSchedule.getDate().getStartDate().toLocalDate(),
                guestSchedule.getDate().getEndDate().toLocalDate(),
                guestSchedule.getDate().getStartDate().toLocalTime(),
                guestSchedule.getDate().getEndDate().toLocalTime(),
                inviteLink
        );
    }
}
