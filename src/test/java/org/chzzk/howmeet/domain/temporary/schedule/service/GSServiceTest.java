package org.chzzk.howmeet.domain.temporary.schedule.service;

import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.temporary.schedule.exception.GSErrorCode.SCHEDULE_NOT_FOUND;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GSServiceTest {

    @InjectMocks
    private GSService gsService;

    @Mock
    private GSRepository gsRepository;

    @Test
    @DisplayName("비회원 일정 조회 시 잘못된 ID로 예외 발생")
    public void getGuestScheduleWhenInvalidId() {
        Long invalidId = 999L;

        // when
        doReturn(Optional.empty()).when(gsRepository).findById(invalidId);

        // then
        assertThatThrownBy(() -> gsService.getGuestSchedule(invalidId))
                .isInstanceOf(GSException.class)
                .hasMessage(SCHEDULE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("비회원 일정 삭제 시 잘못된 ID로 예외 발생")
    public void deleteGuestScheduleWhenInvalidId() {
        // given
        Long invalidId = 999L;

        when(gsRepository.existsById(invalidId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> gsService.deleteGuestSchedule(invalidId))
                .isInstanceOf(GSException.class)
                .hasMessage(SCHEDULE_NOT_FOUND.getMessage());
        verify(gsRepository, times(1)).existsById(invalidId);
    }
}
