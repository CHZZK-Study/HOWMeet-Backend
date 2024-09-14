package org.chzzk.howmeet.fixture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chzzk.howmeet.domain.common.auth.model.Role;
import org.chzzk.howmeet.domain.common.model.Image;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetResponse;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;


public enum MSRecordFixture {

    KIM_MSRECORD_A(MSFixture.createMemberScheduleA(RoomFixture.createRoomA()), createMockMember(), LocalDateTime.of(2023, 1, 1, 10, 30, 0));

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
//        when(member.getId()).thenReturn(1L); // 원하는 ID 값 설정
//        when(member.getNickname()).thenReturn(Nickname.from("김민우")); // 닉네임 설정
//        when(member.getRole()).thenReturn(Role.REGULAR); // 역할 설정
//        when(member.getProfileImage()).thenReturn(Image.from("https://example.com/profile.jpg")); // 프로필 이미지 설정
//        when(member.getSocialId()).thenReturn("social12345"); // 소셜 ID 설정
        return member;
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
