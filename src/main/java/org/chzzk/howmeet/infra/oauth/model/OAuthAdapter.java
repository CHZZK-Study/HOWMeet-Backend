package org.chzzk.howmeet.infra.oauth.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthAdapter {
    public static Map<String, OAuthProvider> getOAuthProviders(final OAuthProperties properties) {
        return properties.getClient()
                .keySet()
                .stream()
                .collect(Collectors.toMap(key -> key, key -> OAuthProvider.of(properties.getClientFrom(key), properties.getProviderFrom(key))));
    }
}
