package org.chzzk.howmeet.domain.common.embedded.date.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleTime {
    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private ScheduleTime(final LocalTime startTime, final LocalTime endTime) {
        validateTimeRange(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ScheduleTime of(LocalTime startTime, LocalTime endTime) {
        return new ScheduleTime(startTime, endTime);
    }

    private void validateTimeRange(final LocalTime startTime, final LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("start time must be before end time");
        }
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}