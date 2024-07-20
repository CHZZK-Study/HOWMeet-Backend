package org.chzzk.howmeet.domain.common.embedded.date.impl;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.embedded.date.BaseDate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleDate extends BaseDate {
    private static final int MAX_PERIOD = 7;

    private ScheduleDate(final LocalDateTime startDate, final LocalDateTime endDate) {
        super(startDate, endDate);
        validateDateRange(startDate, endDate);
    }

    public static ScheduleDate of(LocalDateTime startDate, LocalDateTime endDate) {
        return new ScheduleDate(startDate, endDate);
    }

    private void validateDateRange(final LocalDateTime startDate, final LocalDateTime endDate) {
        if (ChronoUnit.DAYS.between(startDate, endDate) > MAX_PERIOD) {
            throw new IllegalArgumentException("start date must be less than 7 days");
        }
    }
}
