package org.chzzk.howmeet.infra.oauth.dto.token.request;

import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;

public record OAuthTokenRequest(String grant_type,
                                String client_id,
                                String redirect_uri,
                                String code,
                                String client_secret) {
    public static OAuthTokenRequest of(final OAuthProvider provider, final String code) {
        return new OAuthTokenRequest(provider.grantType(), provider.clientId(), provider.redirectUrl(), code, provider.clientSecret());
    }
}
