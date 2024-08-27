package org.chzzk.howmeet.infra.oauth.repository;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.exception.UnsupportedProviderException;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;

import java.util.Map;

@RequiredArgsConstructor
public class InMemoryOAuthProviderRepository {
    private final Map<String, OAuthProvider> providers;

    public OAuthProvider findByProviderName(String providerName) {
        if (!providers.containsKey(providerName)) {
            throw new UnsupportedProviderException(providerName);
        }
        return providers.get(providerName);
    }
}
