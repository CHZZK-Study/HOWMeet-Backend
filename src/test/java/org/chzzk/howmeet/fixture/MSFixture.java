package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

import java.time.LocalTime;
import java.util.List;

public enum MSFixture {
    MEETING_A("Meeting A", List.of("2023-01-01", "2023-01-02"), LocalTime.of(9, 0), LocalTime.of(17, 0)),
    MEETING_B("Meeting B", List.of("2023-02-01", "2023-02-02"), LocalTime.of(10, 0), LocalTime.of(18, 0));

    private final String name;
    private final List<String> dates;
    private final LocalTime startTime;
    private final LocalTime endTime;

    MSFixture(final String name, final List<String> dates, final LocalTime startTime, final LocalTime endTime) {
        this.name = name;
        this.dates = dates;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public MemberSchedule create(Room room) {
        ScheduleTime scheduleTime = ScheduleTime.of(startTime, endTime);
        ScheduleName scheduleName = ScheduleName.from(name);
        return MemberSchedule.of(dates, scheduleTime, scheduleName, room);
    }

    public String getName() {
        return name;
    }

    public List<String> getDates() {
        return dates;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
