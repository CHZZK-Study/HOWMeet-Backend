package org.chzzk.howmeet.infra.oauth.repository;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;

import java.util.Map;

@RequiredArgsConstructor
public class InMemoryOAuthProviderRepository {
    private final Map<String, OAuthProvider> providers;

    public OAuthProvider findByProviderName(String providerName) {
        return providers.get(providerName);
    }
}
