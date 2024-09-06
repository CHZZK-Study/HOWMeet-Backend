package org.chzzk.howmeet.domain.regular.auth.dto.reissue.response;

import org.chzzk.howmeet.domain.regular.auth.dto.reissue.MemberReissueResult;

public record MemberReissueResponse(String accessToken) {
    public static MemberReissueResponse from(final MemberReissueResult memberReissueResult) {
        return new MemberReissueResponse(memberReissueResult.accessToken());
    }
}
