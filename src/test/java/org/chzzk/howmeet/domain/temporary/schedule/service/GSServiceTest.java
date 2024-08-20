package org.chzzk.howmeet.domain.temporary.schedule.service;

import org.chzzk.howmeet.domain.temporary.schedule.dto.GSRequest;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.chzzk.howmeet.fixture.GSFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GSServiceTest {
    @Mock
    GSRepository gsRepository;

    @InjectMocks
    GSService gsService;

    GuestSchedule guestSchedule = GSFixture.MEETING_A.create(1L);
    GSRequest gsRequest = new GSRequest(guestSchedule.getDates(), guestSchedule.getTime(), guestSchedule.getName());
    GSResponse gsResponse = GSResponse.of(guestSchedule, "http://localhost:8080/guest-schedule/invite/" + guestSchedule.getId());

    @Test
    @DisplayName("게스트 일정 생성")
    public void createGuestSchedule() throws Exception {
        // given
        final GSResponse expected = gsResponse;

        // when
        doReturn(guestSchedule).when(gsRepository).save(any(GuestSchedule.class));
        final GSResponse actual = gsService.createGuestSchedule(gsRequest);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("게스트 일정 조회")
    public void getGuestSchedule() throws Exception {
        // given
        final GSResponse expected = gsResponse;

        // when
        doReturn(Optional.of(guestSchedule)).when(gsRepository).findById(guestSchedule.getId());
        final GSResponse actual = gsService.getGuestSchedule(guestSchedule.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("게스트 일정 조회 시 잘못된 ID로 예외 발생")
    public void getGuestScheduleWhenInvalidId() throws Exception {
        // when
        doReturn(Optional.empty()).when(gsRepository).findById(anyLong());

        // then
        assertThatThrownBy(() -> gsService.getGuestSchedule(guestSchedule.getId()))
                .isInstanceOf(GSException.class)
                .hasMessage("스케줄을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("게스트 일정 삭제")
    public void deleteGuestSchedule() throws Exception {
        // given
        Long validId = guestSchedule.getId();

        // when
        doReturn(true).when(gsRepository).existsById(validId);
        doNothing().when(gsRepository).deleteById(validId);
        gsService.deleteGuestSchedule(validId);

        // then
        verify(gsRepository, times(1)).deleteById(validId);
    }

    @Test
    @DisplayName("게스트 일정 삭제 시 잘못된 ID로 예외 발생")
    public void deleteGuestScheduleWhenInvalidId() throws Exception {
        // when
        doReturn(false).when(gsRepository).existsById(anyLong());

        // then
        assertThatThrownBy(() -> gsService.deleteGuestSchedule(guestSchedule.getId()))
                .isInstanceOf(GSException.class)
                .hasMessage("스케줄을 찾을 수 없습니다.");
    }
}
