package org.chzzk.howmeet.mock.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MockMemberProvider {
    private final MemberRepository memberRepository;

    @Transactional
    public Member createMockMember() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        final String nickname = UUID.randomUUID().toString().substring(0, 10);
        final String profileUrl = "profile_url";
        final String socialId = UUID.randomUUID().toString();
        final Member member = Member.of(nickname, profileUrl, socialId);
        return memberRepository.save(member);
    }
}
