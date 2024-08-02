package org.chzzk.howmeet.domain.common.embedded.date.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleDate {
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private static final int MAX_PERIOD = 7;

    private ScheduleDate(final LocalDateTime startDate, final LocalDateTime endDate) {
        validateDateRange(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ScheduleDate of(LocalDateTime startDate, LocalDateTime endDate) {
        return new ScheduleDate(startDate, endDate);
    }

    private void validateDateRange(final LocalDateTime startDate, final LocalDateTime endDate) {
        if (ChronoUnit.DAYS.between(startDate, endDate) > MAX_PERIOD) {
            throw new IllegalArgumentException("start date must be less than 7 days");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("start date must be before end date");
        }
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}