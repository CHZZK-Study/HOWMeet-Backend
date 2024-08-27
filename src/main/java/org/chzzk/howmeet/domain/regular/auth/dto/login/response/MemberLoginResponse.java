package org.chzzk.howmeet.domain.regular.auth.dto.login.response;

import org.chzzk.howmeet.domain.regular.member.entity.Member;

public record MemberLoginResponse(String accessToken, Long memberId, String nickname) {
    public static MemberLoginResponse of(final String accessToken, final Member member) {
        return new MemberLoginResponse(accessToken, member.getId(), member.getNickname().getValue());
    }
}
