package org.chzzk.howmeet.domain.regular.member.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.member.dto.summary.dto.MemberSummaryDto;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberFindService {
    private final MemberRepository memberRepository;

    public Optional<Member> findBySocialId(final String socialId) {
        return memberRepository.findBySocialId(socialId);
    }

    public Optional<MemberSummaryDto> findSummary(final Long memberId) {
        return memberRepository.findSummaryById(memberId);
    }
}
