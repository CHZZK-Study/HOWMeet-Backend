package org.chzzk.howmeet.domain.regular.auth.dto.authorize.response;

import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;

import java.util.List;

public record MemberAuthorizeResponse(String clientId, List<String> scopes, String method, String url) {
    public static MemberAuthorizeResponse from(final OAuthProvider oAuthProvider) {
        return new MemberAuthorizeResponse(oAuthProvider.clientId(), oAuthProvider.scope(), oAuthProvider.authorizeMethod().name(), oAuthProvider.authorizeUrl());
    }
}
