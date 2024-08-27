package org.chzzk.howmeet.domain.regular.member.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.member.dto.summary.dto.MemberSummaryDto;
import org.chzzk.howmeet.domain.regular.member.dto.summary.response.MemberSummaryResponse;
import org.chzzk.howmeet.domain.regular.member.exception.MemberException;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import static org.chzzk.howmeet.domain.regular.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberSummaryResponse getSummary(final AuthPrincipal authPrincipal) {
        final MemberSummaryDto memberSummaryDto = findMemberSummaryById(authPrincipal.id());
        return MemberSummaryResponse.from(memberSummaryDto);
    }

    private MemberSummaryDto findMemberSummaryById(final Long memberId) {
        return memberRepository.findSummaryById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
