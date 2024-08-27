package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSRequest;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;

public enum GSFixture {
    MEETING_A(ScheduleName.from("Meeting A"), List.of("2023-01-01", "2023-01-02"), LocalTime.of(9, 0), LocalTime.of(17, 0)),
    MEETING_B(ScheduleName.from("Meeting B"), List.of("2023-02-01", "2023-02-02"), LocalTime.of(10, 0), LocalTime.of(18, 0));
    //MEETING_C("Meeting C", List.of("2023-03-01", "2023-03-02"), LocalTime.of(11, 0), LocalTime.of(19, 0)),
    //MEETING_D("Meeting D", List.of("2023-04-01", "2023-04-02"), LocalTime.of(12, 0), LocalTime.of(20, 0));

    private final ScheduleName name;
    private final List<String> dates;
    private final LocalTime startTime;
    private final LocalTime endTime;

    GSFixture(final ScheduleName name, final List<String> dates, final LocalTime startTime, final LocalTime endTime) {
        this.name = name;
        this.dates = dates;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static GuestSchedule createGuestScheduleA() {
        ScheduleTime scheduleTime = ScheduleTime.of(MEETING_A.startTime, MEETING_A.endTime);
        GuestSchedule guestSchedule = GuestSchedule.of(MEETING_A.dates, scheduleTime, MEETING_A.name);
        ReflectionTestUtils.setField(guestSchedule, "id", 1L);
        return guestSchedule;
    }

    public static GuestSchedule createGuestScheduleB() {
        ScheduleTime scheduleTime = ScheduleTime.of(MEETING_B.startTime, MEETING_B.endTime);
        GuestSchedule guestSchedule = GuestSchedule.of(MEETING_B.dates, scheduleTime, MEETING_B.name);
        ReflectionTestUtils.setField(guestSchedule, "id", 2L);
        return guestSchedule;
    }

    public static GSRequest createGSRequestA() {
        ScheduleTime scheduleTime = ScheduleTime.of(MEETING_A.startTime, MEETING_A.endTime);
        return new GSRequest(MEETING_A.dates, scheduleTime, MEETING_A.name);
    }

    public static GSRequest createGSRequestB() {
        ScheduleTime scheduleTime = ScheduleTime.of(MEETING_B.startTime, MEETING_B.endTime);
        return new GSRequest(MEETING_B.dates, scheduleTime, MEETING_B.name);
    }

    public static GSResponse createGSResponseA() {
        GuestSchedule guestSchedule = createGuestScheduleA();
        return GSResponse.of(guestSchedule);
    }

    public static GSResponse createGSResponseB() {
        GuestSchedule guestSchedule = createGuestScheduleB();
        return GSResponse.of(guestSchedule);
    }

    public ScheduleName getName() {
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
