package org.chzzk.howmeet.domain.temporary.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;

import java.util.List;

public record GSRequest(List<String> dates, ScheduleTime time, ScheduleName name) {
}