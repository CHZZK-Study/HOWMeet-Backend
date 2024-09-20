package org.chzzk.howmeet.domain.temporary.guest.repository;

import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.chzzk.howmeet.global.config.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class GuestRepositoryTest {
    @Autowired
    GuestRepository guestRepository;

    static Stream<Arguments> guestData() {
        return Stream.of(
                Arguments.of("1", "김민우"),
                Arguments.of("1", "김수현"),
                Arguments.of("1", "이채림"),
                Arguments.of("1", "구예진"),
                Arguments.of("2", "김민우"),
                Arguments.of("2", "김수현"),
                Arguments.of("2", "이채림"),
                Arguments.of("2", "구예진"),
                Arguments.of("3", "김민우"),
                Arguments.of("3", "김수현"),
                Arguments.of("3", "이채림"),
                Arguments.of("3", "구예진")
        );
    }

    @ParameterizedTest
    @DisplayName("일정 ID와 닉네임으로 1회용 회원 조회")
    @MethodSource("guestData")
    public void findByGuestScheduleIdAndNickname(final Long guestScheduleId, final String nickname) throws Exception {
        final Guest guest = guestRepository.findByGuestScheduleIdAndNickname(guestScheduleId, Nickname.from(nickname))
                .get();
        assertThat(guest.getNickname()).isEqualTo(Nickname.from(nickname));
        assertThat(guest.getGuestScheduleId()).isEqualTo(guestScheduleId);
    }

    @ParameterizedTest
    @DisplayName("일정 ID와 닉네임으로 1회용 회원 존재 여부 확인")
    @MethodSource("guestData")
    public void existsByGuestScheduleIdAndNickname(final Long guestScheduleId, final String nickname) throws Exception {
        assertThat(guestRepository.existsByGuestScheduleIdAndNickname(guestScheduleId, Nickname.from(nickname))).isTrue();
    }
}