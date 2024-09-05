package org.chzzk.howmeet.domain.regular.auth.dto.login.response;

import org.chzzk.howmeet.domain.regular.auth.dto.login.MemberLoginResult;

public record MemberLoginResponse(String accessToken, Long memberId, String nickname) {
    public static MemberLoginResponse from(final MemberLoginResult memberLoginResult) {
        return new MemberLoginResponse(memberLoginResult.accessToken(), memberLoginResult.memberId(), memberLoginResult.nickname());
    }
}
