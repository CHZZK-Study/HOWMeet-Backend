package org.chzzk.howmeet.infra.oauth.model;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public record OAuthProvider(String name,
                            String clientId,
                            String clientSecret,
                            String responseType,
                            String authorizeUrl,
                            HttpMethod authorizeMethod,
                            List<String> scope,
                            String redirectUri,
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
                provider.getAuthorize().getResponse_type(),
                provider.getAuthorize().getUrl(),
                HttpMethod.valueOf(provider.getAuthorize().getMethod()),
                new ArrayList<>(client.getScope()),
                client.getRedirectUri(),
                provider.getToken().getIssue().getGrant_type(),
                HttpMethod.valueOf(provider.getToken().getIssue().getMethod()),
                provider.getToken().getIssue().getUrl(),
                HttpMethod.valueOf(provider.getProfile().getMethod()),
                provider.getProfile().getUrl()
        );
    }
}
