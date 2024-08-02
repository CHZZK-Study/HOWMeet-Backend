package org.chzzk.howmeet.domain.temporary.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleDate;
import org.chzzk.howmeet.domain.common.model.ScheduleName;

public record GuestScheduleRequest(Long guestScheduleId, ScheduleDate date, ScheduleName name) {
}
