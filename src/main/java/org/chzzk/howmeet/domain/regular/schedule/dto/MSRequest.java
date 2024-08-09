package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;

import java.util.List;

public record MSRequest(List<String> dates, ScheduleTime time, ScheduleName name, Long roomId) {
}