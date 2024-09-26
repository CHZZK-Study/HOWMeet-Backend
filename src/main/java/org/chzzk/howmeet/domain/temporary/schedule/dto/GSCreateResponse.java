package org.chzzk.howmeet.domain.temporary.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;

import java.util.List;

public record GSCreateResponse(Long gsId) {
    public static GSCreateResponse from(final GuestSchedule guestSchedule) {
        return new GSCreateResponse(
                guestSchedule.getId()
        );
    }
}
