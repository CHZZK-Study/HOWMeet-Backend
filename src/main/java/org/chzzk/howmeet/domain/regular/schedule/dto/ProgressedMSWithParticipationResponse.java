package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.util.List;

public record ProgressedMSWithParticipationResponse(Long id, List<String> dates, ScheduleTime time, ScheduleName name, ScheduleStatus status, boolean isParticipant) implements MSResponse {
    public static ProgressedMSWithParticipationResponse of(final MemberSchedule memberSchedule, boolean isParticipant) {
        return new ProgressedMSWithParticipationResponse(
                memberSchedule.getId(),
                memberSchedule.getDates(),
                memberSchedule.getTime(),
                memberSchedule.getName(),
                memberSchedule.getStatus(),
                isParticipant
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
