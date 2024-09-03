package org.chzzk.howmeet.domain.regular.auth.dto.login;

import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.chzzk.howmeet.domain.regular.member.entity.Member;

public record MemberLoginResult(String accessToken, Long memberId, String nickname, String refreshToken) {
    public static MemberLoginResult of(final String accessToken, final Member member, final RefreshToken refreshToken) {
        return new MemberLoginResult(accessToken, member.getId(), member.getNickname().getValue(), refreshToken.getValue());
    }
}
