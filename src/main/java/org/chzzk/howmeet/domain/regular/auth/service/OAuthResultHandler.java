package org.chzzk.howmeet.domain.regular.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.auth.util.MemberMapper;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.service.MemberFindService;
import org.chzzk.howmeet.domain.regular.member.service.MemberSaveService;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OAuthResultHandler {
    private final MemberFindService memberFindService;
    private final MemberSaveService memberSaveService;
    private final MemberMapper memberMapper;

    @Transactional
    public Member saveOrGet(final OAuthProfile oAuthProfile) {
        return memberFindService.findBySocialId(oAuthProfile.getSocialId())
                .orElseGet(() -> memberSaveService.save(memberMapper.mapFrom(oAuthProfile)));
    }
}
