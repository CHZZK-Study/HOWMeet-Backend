package org.chzzk.howmeet.infra.oauth.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.model.OAuthAdapter;
import org.chzzk.howmeet.infra.oauth.model.OAuthProperties;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.repository.InMemoryOAuthProviderRepository;
import org.chzzk.howmeet.infra.oauth.util.serilaizer.ListToCommaSeparatedStringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
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

    @Bean
    @Qualifier("oauthObjectMapper")
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .addModule(new SimpleModule().addSerializer(List.class, new ListToCommaSeparatedStringSerializer()))
                .build();
    }
}
