package org.chzzk.howmeet.domain.temporary.auth.service;

import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.chzzk.howmeet.domain.temporary.guest.repository.GuestRepository;
import org.chzzk.howmeet.domain.temporary.guest.service.GuestSaveService;
import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.temporary.schedule.exception.GSErrorCode.SCHEDULE_NOT_FOUND;
import static org.chzzk.howmeet.fixture.GuestFixture.KIM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GuestSaveServiceTest {
    @Mock
    GuestRepository guestRepository;

    @Mock
    GSRepository gsRepository;

    @InjectMocks
    GuestSaveService guestSaveService;

    Long guestScheduleId = 1L;
    Guest guest = KIM.생성();
    EncodedPassword encodedPassword = guest.getPassword();
    String nickname = KIM.getNickname();

    @Test
    @DisplayName("1회용 회원 저장")
    public void save() throws Exception {
        // given
        final Guest expect = guest;

        // when
        doReturn(true).when(gsRepository)
                .existsByGuestScheduleId(guestScheduleId);
        doReturn(expect).when(guestRepository)
                .save(any());
        final Guest actual = guestSaveService.save(guestScheduleId, nickname, encodedPassword);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("1회용 회원 저장시 일정 ID가 잘못되면 예외 발생")
    public void saveWhenInvalidGuestScheduleId() throws Exception {
        // when
        doReturn(false).when(gsRepository)
                .existsByGuestScheduleId(guestScheduleId);

        // then
        assertThatThrownBy(() -> guestSaveService.save(guestScheduleId, nickname, encodedPassword))
                .isInstanceOf(GSException.class)
                .hasMessageContaining(SCHEDULE_NOT_FOUND.getMessage());
    }
}