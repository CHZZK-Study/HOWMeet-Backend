package org.chzzk.howmeet.domain.regular.member.repository;

import org.chzzk.howmeet.domain.regular.member.dto.summary.dto.MemberSummaryDto;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.global.config.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @ParameterizedTest
    @DisplayName("소셜 ID로 정기 회원 조회")
    @ValueSource(strings = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})
    public void findBySocialId(final String socialId) throws Exception {
        final Member member = memberRepository.findBySocialId(socialId)
                .get();
        assertThat(member.getSocialId()).isEqualTo(socialId);
    }

    @ParameterizedTest
    @DisplayName("ID로 간단 회원 정보 조회")
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void findSummaryById(final Long id) throws Exception {
        final MemberSummaryDto memberSummaryDto = memberRepository.findSummaryById(id)
                .get();
        assertThat(memberSummaryDto.id()).isEqualTo(id);
    }
}
