package org.chzzk.howmeet.domain.regular.member.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberSaveService {
    private final MemberRepository memberRepository;

    public Member save(final Member member) {
        return memberRepository.save(member);
    }
}
