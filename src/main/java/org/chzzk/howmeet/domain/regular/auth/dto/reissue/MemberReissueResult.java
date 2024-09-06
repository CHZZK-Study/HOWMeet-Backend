package org.chzzk.howmeet.domain.regular.auth.dto.reissue;

import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;

public record MemberReissueResult(String accessToken, String refreshToken) {
    public static MemberReissueResult of(final String accessToken, final RefreshToken refreshToken) {
        return new MemberReissueResult(accessToken, refreshToken.getValue());
    }
}
