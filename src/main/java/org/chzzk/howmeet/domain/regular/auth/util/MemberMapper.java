package org.chzzk.howmeet.domain.regular.auth.util;

import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.springframework.stereotype.Component;

/**
 * OAuth 모듈에서 도메인(Member)를 의존하지 않기 위해 별도로 만든 Mapper 클래스입니다.
 * author: Kim MinWoo
 */
@Component
public class MemberMapper {
    public Member mapFrom(final OAuthProfile oAuthProfile) {
        return Member.of(oAuthProfile.getNickname(), oAuthProfile.getProfileImage(), oAuthProfile.getSocialId());
    }
}
