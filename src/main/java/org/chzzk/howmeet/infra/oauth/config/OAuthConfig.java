package org.chzzk.howmeet.infra.oauth.config;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.model.OAuthAdapter;
import org.chzzk.howmeet.infra.oauth.repository.InMemoryOAuthProviderRepository;
import org.chzzk.howmeet.infra.oauth.model.OAuthProperties;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(OAuthProperties.class)
@RequiredArgsConstructor
public class OAuthConfig {
    private final OAuthProperties properties;

    @Bean
    public InMemoryOAuthProviderRepository inMemoryOAuthProviderRepository() {
        final Map<String, OAuthProvider> providers = OAuthAdapter.getOAuthProviders(properties);
        return new InMemoryOAuthProviderRepository(providers);
    }
}
