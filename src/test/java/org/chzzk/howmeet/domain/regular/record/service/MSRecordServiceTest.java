package org.chzzk.howmeet.domain.regular.record.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetRequest;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetResponse;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.chzzk.howmeet.domain.regular.record.repository.MSRecordRepository;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.chzzk.howmeet.fixture.MSFixture;
import org.chzzk.howmeet.fixture.MemberFixture;
import org.chzzk.howmeet.fixture.RoomFixture;
import org.chzzk.howmeet.fixture.RoomMemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MSRecordServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    MSRecordRepository msRecordRepository;
    @Mock
    MSRepository msRepository;
    @Mock
    RoomMemberRepository roomMemberRepository;
    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    private MSRecordService msRecordService;

    @Test
    @DisplayName("MemberScheduleRecord 입력")
    public void postMSRecord() {
        Member member = MemberFixture.KIM.생성();
        MemberSchedule memberSchedule = MSFixture.createMemberScheduleA(RoomFixture.createRoomA());
        List<LocalDateTime> selectTimes = Arrays.asList(
                LocalDateTime.of(2023, 1, 1, 10, 30),
                LocalDateTime.of(2023, 1, 1, 11, 0),
                LocalDateTime.of(2023, 1, 1, 11, 30)
        );

        MSRecordPostRequest msRecordPostRequest = new MSRecordPostRequest(memberSchedule.getId(), selectTimes);
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, member.getNickname().getValue(), member.getRole());

        when(msRepository.findById(memberSchedule.getId())).thenReturn(Optional.of(memberSchedule));
        when(memberRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return Optional.of(Member.of("김민우", "Kim", "123"));
        });

        msRecordService.postMSRecord(msRecordPostRequest, authPrincipal);

        verify(msRecordRepository).deleteByMemberScheduleIdAndMemberId(memberSchedule.getId(), member.getId());
        verify(msRecordRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("잘못된 일정의 MemberScheduleRecord 입력으로 예외 발생")
    void testPostMSRecord_InvalidDate_ThrowsException() {
        Member member = MemberFixture.KIM.생성();
        MemberSchedule memberSchedule = MSFixture.createMemberScheduleA(RoomFixture.createRoomA());
        List<LocalDateTime> selectTimes = Arrays.asList(
                LocalDateTime.of(2020, 1, 1, 10, 30),
                LocalDateTime.of(2020, 1, 1, 11, 0),
                LocalDateTime.of(2020, 1, 1, 11, 30)
        );

        MSRecordPostRequest msRecordPostRequest = new MSRecordPostRequest(memberSchedule.getId(), selectTimes);
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, member.getNickname().getValue(), member.getRole());

        when(msRepository.findById(memberSchedule.getId())).thenReturn(Optional.of(memberSchedule));
        when(memberRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return Optional.of(Member.of("김민우", "Kim", "123"));
        });

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            msRecordService.postMSRecord(msRecordPostRequest, authPrincipal);
        });

        assertEquals("선택할 수 없는 날짜를 선택하셨습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("MemberScheduleRecord 조회")
    public void getMSRecord() {
        Long msId = 1L;
        Long roomId = 1L;
        Long memberId = 1L;

        Member member = MemberFixture.KIM.생성();
        MemberSchedule memberSchedule = MSFixture.createMemberScheduleA(RoomFixture.createRoomA());
        MemberScheduleRecord msRecord = MemberScheduleRecord.of(member, memberSchedule, LocalDateTime.now());
        Room room = RoomFixture.createRoomA();
        RoomMember roomMember1 = RoomMemberFixture.MEMBER_1.create(room);

        MSRecordGetRequest msRecordGetRequest = new MSRecordGetRequest(msId, roomId);
        AuthPrincipal authPrincipal = new AuthPrincipal(memberId, "Kim", member.getRole());

        List<RoomMember> roomMemberList = Collections.singletonList(roomMember1);
        List<MemberScheduleRecord> msRecords = Collections.singletonList(msRecord);

        when(roomMemberRepository.findByRoomId(roomId)).thenReturn(roomMemberList);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(msRecordRepository.findByMemberScheduleId(msId)).thenReturn(msRecords);

        MSRecordGetResponse msRecordGetResponse = msRecordService.getMSRecord(msRecordGetRequest, authPrincipal);

        assertEquals(msId, msRecordGetResponse.msId(), "MS ID가 일치하지 않습니다.");
        assertEquals(room.getName(), msRecordGetResponse.roomName(), "방 이름이 일치하지 않습니다.");
        assertTrue("총 인원에 회원 닉네임이 포함되어 있지 않습니다.",
                msRecordGetResponse.totalPersonnel().getNicknames().contains(member.getNickname().getValue()));
        assertTrue("참여 인원에 회원 닉네임이 포함되어 있지 않습니다.",
                msRecordGetResponse.participatedPersonnel().getNicknames().contains(member.getNickname().getValue()));
        assertFalse("선택 시간 목록이 비어있습니다.", msRecordGetResponse.selectTime().isEmpty());

    }


}
