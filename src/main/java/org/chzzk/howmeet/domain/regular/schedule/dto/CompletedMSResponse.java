package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CompletedMSResponse(Long id, List<String> dates, ScheduleTime time, List<String> confirmedDates, ScheduleTime confirmedTime,ScheduleName name, ScheduleStatus status) implements MSResponse {
    public static CompletedMSResponse of(final MemberSchedule memberSchedule, List<String> confirmedDates, ScheduleTime confirmedTime) {
        return new CompletedMSResponse(
                memberSchedule.getId(),
                memberSchedule.getDates(),
                memberSchedule.getTime(),
                confirmedDates,
                confirmedTime,
                memberSchedule.getName(),
                memberSchedule.getStatus()
        );
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public ScheduleName getName() {
        return this.name;
    }

    @Override
    public ScheduleStatus getStatus() {
        return this.status;
    }
}
