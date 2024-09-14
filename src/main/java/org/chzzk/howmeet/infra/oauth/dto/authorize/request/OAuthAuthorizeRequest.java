package org.chzzk.howmeet.infra.oauth.dto.authorize.request;

import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public record OAuthAuthorizeRequest(String client_id, String response_type, List<String> scope, String redirect_uri) {
    public static OAuthAuthorizeRequest from(final OAuthProvider oAuthProvider) {
        return new OAuthAuthorizeRequest(oAuthProvider.clientId(), oAuthProvider.responseType(), oAuthProvider.scope(), URLEncoder.encode(oAuthProvider.redirectUrl(), StandardCharsets.UTF_8));
    }
}
