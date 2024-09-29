package org.chzzk.howmeet.fixture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetResponse;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.test.util.ReflectionTestUtils;


public enum MSRecordFixture {

    KIM_MSRECORD_A(createMockMemberSchedule(), createMockMember(), LocalDateTime.of(2023, 1, 1, 10, 30, 0));

    private final Member member;
    private final MemberSchedule ms;
    private final LocalDateTime selectTime;

    MSRecordFixture(final MemberSchedule ms, final Member member, final LocalDateTime selectTime) {
        this.ms = ms;
        this.member = member;
        this.selectTime = selectTime;
    }

    private static Member createMockMember() {
        Member member = mock(Member.class);

        return member;
    }

    private static MemberSchedule createMockMemberSchedule() {
        Room room = new Room(RoomName.from("테스트 룸"));
        List<RoomMember> members = List.of(
                RoomMemberFixture.MEMBER_1.create(room),
                RoomMemberFixture.MEMBER_2.create(room),
                RoomMemberFixture.MEMBER_3.create(room)
        );



        List<String> dates = List.of("2023-01-01", "2023-01-02");
        ScheduleTime scheduleTime = ScheduleTime.of(LocalTime.of(9, 0), LocalTime.of(17,0));
        MemberSchedule memberSchedule = MemberSchedule.of(dates, scheduleTime, ScheduleName.from("테스트 일정"), room);
        room.updateSchedules(List.of(memberSchedule));
        room.updateMembers(members);
        ReflectionTestUtils.setField(room, "id" ,1L);
        ReflectionTestUtils.setField(memberSchedule, "id" ,1L);
        return memberSchedule;
    }
    public static MSRecordPostRequest createMSRecordPostRequestA() {
        List<LocalDateTime> selectTimes = Arrays.asList(
                LocalDateTime.of(2023, 1, 1, 10, 30),
                LocalDateTime.of(2023, 1, 1, 11, 0),
                LocalDateTime.of(2023, 1, 1, 11, 30)
        );

        return new MSRecordPostRequest(1L, selectTimes);
    }
    public static MSRecordGetResponse createMSRecordGetResponseA() {
        RoomName roomName = RoomName.from("회의실 A");  // 방 이름 예시

        // 총 인원 닉네임 설정
        Nicknames totalPersonnel = Nicknames.create();
        totalPersonnel.add(Nickname.from("김민우"));
        totalPersonnel.add(Nickname.from("이수진"));

        // 참여 인원 닉네임 설정
        Nicknames participatedPersonnel = Nicknames.create();
        participatedPersonnel.add(Nickname.from("김민우"));

        // 선택된 시간과 참여자 정보 설정
        Map<LocalDateTime, Nicknames> selectTimeMap = new HashMap<>();
        selectTimeMap.put(LocalDateTime.of(2023, 1, 1, 10, 30), participatedPersonnel);
        List<SelectionDetail> time = SelectionDetail.convertMapToSelectionDetailsList(selectTimeMap);

        // MSRecordGetResponse 객체 생성 및 반환
        return MSRecordGetResponse.of(1L, roomName, totalPersonnel, participatedPersonnel, time);
    }


    public MemberScheduleRecord create() {
        return MemberScheduleRecord.of(this.member.getId(), this.ms, this.selectTime);
    }
}
