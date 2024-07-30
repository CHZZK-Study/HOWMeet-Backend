package org.chzzk.howmeet.domain.common.embedded.date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseDate {
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    protected BaseDate(final LocalDateTime startDate, final LocalDateTime endDate) {
        validateDateRange(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateDateRange(final LocalDateTime startDate, final LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("start date must be after end date");
        }
    }
}
