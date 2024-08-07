package org.chzzk.howmeet.infra.oauth.model;

import org.springframework.http.HttpMethod;

public record OAuthProvider(String name,
                            String clientId,
                            String clientSecret,
                            String redirectUrl,
                            String grantType,
                            HttpMethod tokenMethod,
                            String tokenUrl,
                            HttpMethod profileMethod,
                            String profileUrl) {
    public static OAuthProvider of(final OAuthProperties.Client client, final OAuthProperties.Provider provider) {
        return new OAuthProvider(
                provider.getName(),
                client.getId(),
                client.getSecret(),
                client.getRedirectUrl(),
                provider.getToken().getIssue().getGrant_type(),
                HttpMethod.valueOf(provider.getToken().getIssue().getMethod()),
                provider.getToken().getIssue().getUrl(),
                HttpMethod.valueOf(provider.getProfile().getMethod()),
                provider.getProfile().getUrl()
        );
    }
}
