//package org.chzzk.howmeet.domain.temporary.schedule.service;
//
//import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
//import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
//import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
//import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.chzzk.howmeet.domain.temporary.schedule.exception.GSErrorCode.SCHEDULE_NOT_FOUND;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class GSServiceTest {
//
//    @InjectMocks
//    private GSService gsService;
//
//    @Mock
//    private GSRepository gsRepository;
//
//    @Test
//    @DisplayName("비회원 일정 조회 시 잘못된 ID로 예외 발생")
//    public void getGuestScheduleWhenInvalidId() {
//        Long invalidId = 999L;
//
//        // when
//        doReturn(Optional.empty()).when(gsRepository).findById(invalidId);
//
//        // then
//        assertThatThrownBy(() -> gsService.getGuestSchedule(invalidId))
//                .isInstanceOf(GSException.class)
//                .hasMessage(SCHEDULE_NOT_FOUND.getMessage());
//    }
//
//    @Test
//    @DisplayName("10일 이상 지난 PROGRESS 상태의 스케줄 disable 처리")
//    public void disableProgressGuestSchedule() {
//        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
//
//        GuestSchedule progressSchedule = mock(GuestSchedule.class);
//        lenient().when(progressSchedule.getStatus()).thenReturn(ScheduleStatus.PROGRESS);
//        lenient().when(progressSchedule.getCreatedAt()).thenReturn(tenDaysAgo); // 10일 전으로 설정
//
//        when(gsRepository.findByStatusAndCreatedAtBefore(eq(ScheduleStatus.PROGRESS), any()))
//                .thenReturn(Collections.singletonList(progressSchedule));
//
//        gsService.disableOldGuestSchedules();
//
//        verify(progressSchedule).deactivate();
//    }
//
//    @Test
//    @DisplayName("10일 이상 지난 COMPLETE 상태의 스케줄 disable 처리")
//    public void disableCompleteGuestSchedule() {
//        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
//
//        GuestSchedule completeSchedule = mock(GuestSchedule.class);
//        lenient().when(completeSchedule.getStatus()).thenReturn(ScheduleStatus.COMPLETE);
//        lenient().when(completeSchedule.getUpdatedAt()).thenReturn(tenDaysAgo);
//
//        when(gsRepository.findByStatusAndUpdatedAtBefore(eq(ScheduleStatus.COMPLETE), any()))
//                .thenReturn(Collections.singletonList(completeSchedule));
//
//        gsService.disableOldGuestSchedules();
//
//        verify(completeSchedule).deactivate();
//    }
//}
