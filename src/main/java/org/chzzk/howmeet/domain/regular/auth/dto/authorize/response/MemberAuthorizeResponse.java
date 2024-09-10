package org.chzzk.howmeet.domain.regular.auth.dto.authorize.response;

import org.chzzk.howmeet.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;

public record MemberAuthorizeResponse(String method, String url) {
    public static MemberAuthorizeResponse from(final OAuthAuthorizePayload oAuthAuthorizePayload) {
        return new MemberAuthorizeResponse(oAuthAuthorizePayload.method().name(), oAuthAuthorizePayload.url().toString());
    }
}
