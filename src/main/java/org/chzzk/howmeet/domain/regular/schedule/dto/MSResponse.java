package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

import java.util.List;

public record MSResponse(Long id, List<String> dates, ScheduleTime time, ScheduleName name, String inviteLink) {

    public static MSResponse of(MemberSchedule memberSchedule, String inviteLink) {
        return new MSResponse(
                memberSchedule.getId(),
                memberSchedule.getDates(),
                memberSchedule.getTime(),
                memberSchedule.getName(),
                inviteLink
        );
    }
}