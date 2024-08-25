package org.chzzk.howmeet.domain.common.embedded.date.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleDate {
    @Column(name = "date")
    private LocalDate date;

    private ScheduleDate(final LocalDate date) {
        this.date = date;
    }

    public static ScheduleDate of(LocalDate date) {
        return new ScheduleDate(date);
    }

    public LocalDate getDate() {
        return date;
    }
}