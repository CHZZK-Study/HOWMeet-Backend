package org.chzzk.howmeet.domain.temporary.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleDate;
import org.chzzk.howmeet.domain.common.model.ScheduleName;

public record GSRequest(ScheduleDate date, ScheduleName name) {
}