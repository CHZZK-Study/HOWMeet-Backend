package org.chzzk.howmeet.domain.common.embedded.date.impl;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.embedded.date.BaseDate;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordDate extends BaseDate {
    public RecordDate(final LocalDateTime startDate, final LocalDateTime endDate) {
        super(startDate, endDate);
        validateDateRange(startDate, endDate);
    }

    public static RecordDate of(LocalDateTime startDate, LocalDateTime endDate) {
        return new RecordDate(startDate, endDate);
    }

    private void validateDateRange(final LocalDateTime startDate, final LocalDateTime endDate) {
        // todo 7/19 김민우 : 시작/종료일 검증 시 해당하는 일정의 시작/종료일이 필요함. 이는 서비스 레이어에서 제공하는게 어떨까?
    }
}
