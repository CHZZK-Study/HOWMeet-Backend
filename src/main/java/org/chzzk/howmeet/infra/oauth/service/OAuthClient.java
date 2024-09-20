package org.chzzk.howmeet.infra.oauth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.chzzk.howmeet.infra.oauth.repository.InMemoryOAuthProviderRepository;
import org.chzzk.howmeet.infra.oauth.service.authorize.OAuthAuthorizeService;
import org.chzzk.howmeet.infra.oauth.service.profile.OAuthProfileService;
import org.chzzk.howmeet.infra.oauth.service.token.OAuthTokenService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OAuthClient {
    private final InMemoryOAuthProviderRepository inMemoryOAuthProviderRepository;
    private final OAuthAuthorizeService oAuthAuthorizeService;
    private final OAuthProfileService oAuthProfileService;
    private final OAuthTokenService oAuthTokenService;

    public OAuthAuthorizePayload getAuthorizePayload(final String providerName) {
        validateProviderName(providerName);
        final OAuthProvider oAuthProvider = getProviderByName(providerName);
        return oAuthAuthorizeService.getAuthorizePayload(oAuthProvider);
    }

    public Mono<OAuthProfile> getProfile(final String providerName, final String code) {
        validateAuthorizationCode(code);
        validateProviderName(providerName);
        final OAuthProvider oAuthProvider = getProviderByName(providerName);
        return oAuthTokenService.getToken(oAuthProvider, code)
                .flatMap(response -> oAuthProfileService.getProfile(oAuthProvider, response));
    }

    private OAuthProvider getProviderByName(final String providerName) {
        return inMemoryOAuthProviderRepository.findByProviderName(providerName);
    }

    private void validateAuthorizationCode(final String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException();   // 예외 처리 커스텀 예정
        }
    }

    private void validateProviderName(final String providerName) {
        if (providerName == null || providerName.isBlank()) {
            throw new IllegalArgumentException();   // 예외 처리 커스텀 예정
        }
    }
}
