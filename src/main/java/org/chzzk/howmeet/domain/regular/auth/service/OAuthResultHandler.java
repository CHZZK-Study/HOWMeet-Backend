package org.chzzk.howmeet.domain.regular.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.auth.entity.Member;
import org.chzzk.howmeet.domain.regular.auth.repository.MemberRepository;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OAuthResultHandler {
    private final MemberRepository memberRepository;

    @Transactional
    public Member saveOrGet(final OAuthProfile oAuthProfile) {
        return memberRepository.findBySocialId(oAuthProfile.getSocialId())
                        .orElseGet(() -> memberRepository.save(oAuthProfile.toEntity()));
    }
}
