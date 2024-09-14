package org.chzzk.howmeet.infra.oauth.dto.authorize.request;

import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;

import java.util.List;

public record OAuthAuthorizeRequest(String client_id, String response_type, List<String> scope) {
    public static OAuthAuthorizeRequest from(final OAuthProvider oAuthProvider) {
        return new OAuthAuthorizeRequest(oAuthProvider.clientId(), oAuthProvider.responseType(), oAuthProvider.scope());
    }
}
