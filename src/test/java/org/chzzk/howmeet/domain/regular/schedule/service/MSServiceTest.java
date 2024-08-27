package org.chzzk.howmeet.domain.regular.schedule.service;

import org.chzzk.howmeet.domain.regular.schedule.exception.MSException;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.SCHEDULE_NOT_FOUND;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MSServiceTest {
    @Mock
    private MSRepository msRepository;

    @InjectMocks
    MSService msService;

    @Test
    @DisplayName("멤버 일정 조회 시 잘못된 ID로 예외 발생")
    public void getMemberScheduleWhenInvalidId() throws Exception {
        // given
        Long invalidId = 999L;

        // when
        doReturn(Optional.empty()).when(msRepository).findById(invalidId);

        // then
        assertThatThrownBy(() -> msService.getMemberSchedule(invalidId))
                .isInstanceOf(MSException.class)
                .hasMessage(SCHEDULE_NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("멤버 일정 삭제 시 잘못된 ID로 예외 발생")
    public void deleteMemberScheduleWhenInvalidId() throws Exception {
        // given
        Long invalidId = 999L;

        // when
        doReturn(Optional.empty()).when(msRepository).findById(invalidId);

        // then
        assertThatThrownBy(() -> msService.deleteMemberSchedule(invalidId))
                .isInstanceOf(MSException.class)
                .hasMessage(SCHEDULE_NOT_FOUND.getMessage());
    }
}
