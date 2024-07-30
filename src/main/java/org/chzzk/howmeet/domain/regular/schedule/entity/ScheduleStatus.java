package org.chzzk.howmeet.domain.regular.schedule.entity;

import lombok.Getter;

@Getter
public enum ScheduleStatus {
    PROGRESS("진행 중"),
    COMPLETE("완료");

    private final String value;

    ScheduleStatus(final String value) {
        this.value = value;
    }
}
