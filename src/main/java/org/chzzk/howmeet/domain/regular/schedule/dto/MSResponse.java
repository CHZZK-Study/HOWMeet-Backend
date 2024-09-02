package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.util.List;

public record MSResponse(Long id, List<String> dates, ScheduleTime time, ScheduleName name, ScheduleStatus status) {

    public static MSResponse from(final MemberSchedule memberSchedule) {
        return new MSResponse(
                memberSchedule.getId(),
                memberSchedule.getDates(),
                memberSchedule.getTime(),
                memberSchedule.getName(),
                memberSchedule.getStatus()
        );
    }
}