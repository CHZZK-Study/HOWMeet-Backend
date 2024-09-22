package org.chzzk.howmeet.domain.temporary.guest.service;

import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.chzzk.howmeet.domain.temporary.guest.repository.GuestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.chzzk.howmeet.fixture.GuestFixture.KIM;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GuestFindServiceTest {
    @Mock
    GuestRepository guestRepository;

    @InjectMocks
    GuestFindService guestFindService;

    Long guestScheduleId = 1L;
    Guest guest = KIM.생성();
    String nickname = KIM.getNickname();

    @Test
    @DisplayName("")
    public void find() throws Exception {
        // given
        final Guest expect = guest;

        // when
        doReturn(Optional.of(expect)).when(guestRepository)
                .findByGuestScheduleIdAndNickname(guestScheduleId, Nickname.from(nickname));
        final Guest actual = guestFindService.find(guestScheduleId, nickname)
                .get();

        // then
        assertThat(actual).isEqualTo(expect);
    }
}