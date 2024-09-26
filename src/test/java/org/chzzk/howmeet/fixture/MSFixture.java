// package org.chzzk.howmeet.fixture;

// import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
// import org.chzzk.howmeet.domain.common.model.ScheduleName;
// import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
// import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
// import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
// import org.chzzk.howmeet.domain.regular.room.entity.Room;
// import org.springframework.test.util.ReflectionTestUtils;

// import java.time.LocalTime;
// import java.util.List;

// public enum MSFixture {
//     MEETING_A(ScheduleName.from("Meeting A"), List.of("2023-01-01", "2023-01-02"), LocalTime.of(9, 0), LocalTime.of(17, 0)),
//     MEETING_B(ScheduleName.from("Meeting B"), List.of("2023-02-01", "2023-02-02"), LocalTime.of(10, 0), LocalTime.of(18, 0));

//     private final ScheduleName name;
//     private final List<String> dates;
//     private final LocalTime startTime;
//     private final LocalTime endTime;

//     MSFixture(final ScheduleName name, final List<String> dates, final LocalTime startTime, final LocalTime endTime) {
//         this.name = name;
//         this.dates = dates;
//         this.startTime = startTime;
//         this.endTime = endTime;
//     }

//     public static MemberSchedule createMemberScheduleA(Room room) {
//         ScheduleTime scheduleTime = ScheduleTime.of(MEETING_A.startTime, MEETING_A.endTime);
//         MemberSchedule memberSchedule = MemberSchedule.of(MEETING_A.dates, scheduleTime, MEETING_A.name, room);
//         ReflectionTestUtils.setField(memberSchedule, "id", 1L);
//         return memberSchedule;
//     }

//     public static MemberSchedule createMemberScheduleB(Room room) {
//         ScheduleTime scheduleTime = ScheduleTime.of(MEETING_B.startTime, MEETING_B.endTime);
//         MemberSchedule memberSchedule = MemberSchedule.of(MEETING_B.dates, scheduleTime, MEETING_B.name, room);
//         ReflectionTestUtils.setField(memberSchedule, "id", 2L);
//         return memberSchedule;
//     }

//     public static MSRequest createMSRequestA() {
//         ScheduleTime scheduleTime = ScheduleTime.of(MEETING_A.startTime, MEETING_A.endTime);
//         return new MSRequest(MEETING_A.dates, scheduleTime, MEETING_A.name);
//     }

//     public static MSRequest createMSRequestB() {
//         ScheduleTime scheduleTime = ScheduleTime.of(MEETING_B.startTime, MEETING_B.endTime);
//         return new MSRequest(MEETING_B.dates, scheduleTime, MEETING_B.name);
//     }

//     public static MSResponse createMSResponseA(Room room) {
//         MemberSchedule memberSchedule = createMemberScheduleA(room);
//         return MSResponse.from(memberSchedule);
//     }

//     public static MSResponse createMSResponseB(Room room) {
//         MemberSchedule memberSchedule = createMemberScheduleB(room);
//         return MSResponse.from(memberSchedule);
//     }

//     public ScheduleName getName() {
//         return name;
//     }

//     public List<String> getDates() {
//         return dates;
//     }

//     public LocalTime getStartTime() {
//         return startTime;
//     }

//     public LocalTime getEndTime() {
//         return endTime;
//     }
// }
