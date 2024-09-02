package org.chzzk.howmeet.domain.regular.member.service;


import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.member.dto.summary.dto.MemberSummaryDto;
import org.chzzk.howmeet.domain.regular.member.dto.summary.response.MemberSummaryResponse;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.regular.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.chzzk.howmeet.fixture.MemberFixture.KIM;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    MemberFindService memberFindService;

    @InjectMocks
    MemberService memberService;

    Member member = KIM.생성();

    @Test
    @DisplayName("토큰 페이로드로 간단 회원 정보 조회")
    public void getSummary() throws Exception {
        // given
        final AuthPrincipal authPrincipal = AuthPrincipal.from(member);
        final MemberSummaryDto memberSummaryDto = new MemberSummaryDto(member.getId(), member.getNickname());
        final MemberSummaryResponse expect = MemberSummaryResponse.from(memberSummaryDto);

        // when
        doReturn(Optional.of(memberSummaryDto)).when(memberFindService).findSummaryByMemberId(authPrincipal.id());
        final MemberSummaryResponse actual = memberService.getSummary(authPrincipal);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("토큰 페이로드에 해당하는 회원이 없는 경우 예외 발생")
    public void getSummaryWhenNotFound() throws Exception {
        // given
        final AuthPrincipal authPrincipal = AuthPrincipal.from(member);

        // when
        doReturn(Optional.empty()).when(memberFindService).findSummaryByMemberId(authPrincipal.id());

        // then
        assertThatThrownBy(() -> memberService.getSummary(authPrincipal))
                .isInstanceOf(MemberException.class)
                .hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
    }
}