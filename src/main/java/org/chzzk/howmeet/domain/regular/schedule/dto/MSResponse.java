package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

public interface MSResponse {
    Long getId();
    ScheduleName getName();
    ScheduleStatus getStatus();
}
