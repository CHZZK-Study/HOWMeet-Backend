package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ProgressedMSResponse(Long id, List<String> dates, ScheduleTime time, ScheduleName name, ScheduleStatus status) implements MSResponse {
    public static ProgressedMSResponse from(final MemberSchedule memberSchedule) {
        return new ProgressedMSResponse(
                memberSchedule.getId(),
                memberSchedule.getDates(),
                memberSchedule.getTime(),
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
