package org.chzzk.howmeet.infra.oauth.dto.authorize.response;

import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.springframework.http.HttpMethod;

import java.net.URI;

public record OAuthAuthorizePayload(HttpMethod method, URI url) {
    public static OAuthAuthorizePayload of(OAuthProvider oAuthProvider, URI uri) {
        return new OAuthAuthorizePayload(oAuthProvider.authorizeMethod(), uri);
    }
}