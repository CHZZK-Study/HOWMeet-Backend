package org.chzzk.howmeet.domain.regular.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.auth.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfileFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OAuthResultHandler {
    private final MemberRepository memberRepository;

    @Transactional
    public Member saveOrGet(final Map<String, Object> attributes, final String providerName) {
        final OAuthProfile oAuthProfile = OAuthProfileFactory.of(attributes, providerName);
        return memberRepository.findBySocialId(oAuthProfile.getSocialId())
                        .orElseGet(() -> memberRepository.save(oAuthProfile.toEntity()));
    }
}
