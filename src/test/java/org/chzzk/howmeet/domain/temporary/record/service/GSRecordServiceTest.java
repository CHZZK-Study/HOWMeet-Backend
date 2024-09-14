package org.chzzk.howmeet.domain.temporary.record.service;

import static org.chzzk.howmeet.domain.temporary.auth.exception.GuestErrorCode.GUEST_NOT_FOUND;
import static org.chzzk.howmeet.domain.temporary.record.exception.GSRecordErrorCode.DATE_INVALID_SELECT;
import static org.chzzk.howmeet.domain.temporary.record.exception.GSRecordErrorCode.TIME_INVALID_SELECT;
import static org.chzzk.howmeet.domain.temporary.schedule.exception.GSErrorCode.SCHEDULE_NOT_FOUND;
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
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.auth.exception.GuestException;
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
import org.chzzk.howmeet.domain.temporary.record.dto.get.response.GSRecordGetResponse;
import org.chzzk.howmeet.domain.temporary.record.dto.post.request.GSRecordPostRequest;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;
import org.chzzk.howmeet.domain.temporary.record.exception.GSRecordException;
import org.chzzk.howmeet.domain.temporary.record.repository.GSRecordRepository;
import org.chzzk.howmeet.domain.temporary.record.repository.TmpGuestRepository;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.chzzk.howmeet.fixture.GSFixture;
import org.chzzk.howmeet.fixture.GuestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GSRecordServiceTest {

    @Mock
    GSRecordRepository gsRecordRepository;
    @Mock
    GSRepository gsRepository;
    @Mock
    GuestRepository guestRepository;
    @Mock
    TmpGuestRepository tmpGuestRepository;

    @InjectMocks
    GSRecordService gsRecordService;

    @Test
    @DisplayName("GuestScheduleRecord 입력")
    public void postGSRecord() throws Exception {
        Guest guest = GuestFixture.KIM.생성();
        GuestSchedule guestSchedule = GSFixture.createGuestScheduleA();
        List<LocalDateTime> selectTimes = Arrays.asList(
                LocalDateTime.of(2023, 1, 1, 10, 30),
                LocalDateTime.of(2023, 1, 1, 11, 00),
                LocalDateTime.of(2023, 1, 1, 11, 30)
        );

        GSRecordPostRequest gsRecordPostRequest = new GSRecordPostRequest(1L, selectTimes);
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, guest.getNickname().getValue(), guest.getRole());

        when(gsRepository.findById(guestSchedule.getId())).thenReturn(Optional.of(guestSchedule));
        when(guestRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return Optional.of(Guest.of(1L, "Kim", guest.getPassword()));
        });

        gsRecordService.postGSRecord(gsRecordPostRequest, authPrincipal);

        verify(gsRecordRepository).deleteByGuestId(guest.getId());
        verify(gsRecordRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("잘못된 일정의 GuestScheduleRecord의 날짜 입력으로 예외 발생")
    public void postGSRecord_InvalidDATE() throws Exception {

        Guest guest = GuestFixture.KIM.생성();
        GuestSchedule guestSchedule = GSFixture.createGuestScheduleA();
        List<LocalDateTime> selectTimes = Arrays.asList(
                LocalDateTime.of(2024, 1, 1, 10, 30),
                LocalDateTime.of(2024, 1, 1, 11, 00),
                LocalDateTime.of(2024, 1, 1, 11, 30)
        );

        GSRecordPostRequest gsRecordPostRequest = new GSRecordPostRequest(1L, selectTimes);
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, guest.getNickname().getValue(), guest.getRole());

        when(gsRepository.findById(guestSchedule.getId())).thenReturn(Optional.of(guestSchedule));
        when(guestRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return Optional.of(Guest.of(1L, "Kim", guest.getPassword()));
        });

        GSRecordException exception = assertThrows(GSRecordException.class, () -> {
            gsRecordService.postGSRecord(gsRecordPostRequest, authPrincipal);
        });

        assertEquals(DATE_INVALID_SELECT.getMessage(), exception.getMessage());
        assertEquals(DATE_INVALID_SELECT.getStatus(), exception.getStatus());
    }


    @Test
    @DisplayName("잘못된 일정의 GuestScheduleRecord의 시간 입력으로 예외 발생")
    public void postGSRecord_InvalidTime() throws Exception {

        Guest guest = GuestFixture.KIM.생성();
        GuestSchedule guestSchedule = GSFixture.createGuestScheduleA();
        List<LocalDateTime> selectTimes = Arrays.asList(
//                LocalDateTime.of(2023, 1, 1, 10, 30),
//                LocalDateTime.of(2023, 1, 1, 9, 0),
                LocalDateTime.of(2023, 1, 1, 17, 00)
        );

        GSRecordPostRequest gsRecordPostRequest = new GSRecordPostRequest(1L, selectTimes);
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, guest.getNickname().getValue(), guest.getRole());

        when(gsRepository.findById(guestSchedule.getId())).thenReturn(Optional.of(guestSchedule));
        when(guestRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return Optional.of(Guest.of(1L, "Kim", guest.getPassword()));
        });

        GSRecordException exception = assertThrows(GSRecordException.class, () -> {
            gsRecordService.postGSRecord(gsRecordPostRequest, authPrincipal);
        });

        assertEquals(TIME_INVALID_SELECT.getMessage(), exception.getMessage());
        assertEquals(TIME_INVALID_SELECT.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("존재하지 않는 GuestSchedule에 대한 예외 발생")
    public void postGSRecord_GuestScheduleNotFound() throws Exception {
        Guest guest = GuestFixture.KIM.생성();
        List<LocalDateTime> selectTimes = Arrays.asList(
                LocalDateTime.of(2023, 1, 1, 10, 30),
                LocalDateTime.of(2023, 1, 1, 11, 0),
                LocalDateTime.of(2023, 1, 1, 11, 30)
        );

        GSRecordPostRequest gsRecordPostRequest = new GSRecordPostRequest(529L, selectTimes);
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, guest.getNickname().getValue(), guest.getRole());

        when(gsRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(guest));

        GSException exception = assertThrows(GSException.class, () -> {
            gsRecordService.postGSRecord(gsRecordPostRequest, authPrincipal);
        });

        assertEquals(SCHEDULE_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals(SCHEDULE_NOT_FOUND.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("존재하지 않는 Guest에 대한 예외 발생")
    public void postGSRecord_GuestNotFound() throws Exception {
        Guest guest = GuestFixture.KIM.생성();
        GuestSchedule guestSchedule = GSFixture.createGuestScheduleA();
        List<LocalDateTime> selectTimes = Arrays.asList(
                LocalDateTime.of(2023, 1, 1, 10, 30),
                LocalDateTime.of(2023, 1, 1, 11, 00),
                LocalDateTime.of(2023, 1, 1, 11, 30)
        );

        GSRecordPostRequest gsRecordPostRequest = new GSRecordPostRequest(1L, selectTimes);
        AuthPrincipal authPrincipal = new AuthPrincipal(320L, guest.getNickname().getValue(), guest.getRole());

        when(guestRepository.findById(anyLong())).thenReturn(Optional.empty());

        GuestException exception = assertThrows(GuestException.class, () -> {
            gsRecordService.postGSRecord(gsRecordPostRequest, authPrincipal);
        });

        assertEquals(GUEST_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals(GUEST_NOT_FOUND.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("GuestScheduleRecord 조회")
    public void getMSRecord() {
        Long gsId = 1L;
        Long guestId = 1L;

        Guest guest = GuestFixture.KIM.생성();
        GuestSchedule guestSchedule = GSFixture.createGuestScheduleA();
        GuestScheduleRecord gsReord = GuestScheduleRecord.of(guest, guestSchedule, LocalDateTime.now());

        List<GuestScheduleRecord> gsRecords = Collections.singletonList(gsReord);
        List<Guest> guestList = Collections.singletonList(guest);

        when(tmpGuestRepository.findByGuestScheduleId(gsId)).thenReturn(guestList);
        when(gsRecordRepository.findByGuestScheduleId(gsId)).thenReturn(gsRecords);

        GSRecordGetResponse gsRecordGetResponse = gsRecordService.getGSRecord(gsId);

        assertEquals(gsId, gsRecordGetResponse.gsId(), "GS ID가 일치하지 않습니다.");
        assertTrue("총 인원에 회원 닉네임이 포함되어 있지 않습니다.", gsRecordGetResponse.totalPersonnel().contains(guest.getNickname()));
        assertTrue("참여 인원에 회원 닉네임이 포함되어 있지 않습니다.", gsRecordGetResponse.participatedPersonnel().contains(guest.getNickname()));
        assertFalse("선택 시간 목록이 비어있습니다.", gsRecordGetResponse.time().isEmpty());

    }
}
