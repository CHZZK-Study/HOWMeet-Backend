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

    public ScheduleTime(final LocalTime startTime, final LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ScheduleTime of(final LocalTime startTime, final LocalTime endTime) {
        return new ScheduleTime(startTime, endTime);
    }

    public boolean isContainsMidnight() {
        return startTime.isAfter(endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}