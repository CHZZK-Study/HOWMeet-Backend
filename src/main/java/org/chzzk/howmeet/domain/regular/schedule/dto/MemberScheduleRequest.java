package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleDate;
import org.chzzk.howmeet.domain.common.model.ScheduleName;

public record MemberScheduleRequest(Long memberScheduleId, ScheduleDate date, ScheduleName name) {
}