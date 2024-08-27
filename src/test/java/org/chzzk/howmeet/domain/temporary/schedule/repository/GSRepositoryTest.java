package org.chzzk.howmeet.domain.temporary.schedule.repository;

import org.chzzk.howmeet.global.config.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class GSRepositoryTest {
    @Autowired
    GSRepository gsRepository;

    @ParameterizedTest
    @DisplayName("ID로 비회원 일정 확인")
    @ValueSource(longs = {1L, 2L, 3L})
    public void existsByGuestScheduleId(final Long guestScheduleId) throws Exception {
        assertThat(gsRepository.existsByGuestScheduleId(guestScheduleId)).isTrue();
    }
}