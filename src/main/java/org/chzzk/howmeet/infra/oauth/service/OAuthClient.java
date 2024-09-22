package org.chzzk.howmeet.infra.oauth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import org.chzzk.howmeet.infra.oauth.exception.param.OAuthParamException;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.chzzk.howmeet.infra.oauth.repository.InMemoryOAuthProviderRepository;
import org.chzzk.howmeet.infra.oauth.service.authorize.OAuthAuthorizeService;
import org.chzzk.howmeet.infra.oauth.service.profile.OAuthProfileService;
import org.chzzk.howmeet.infra.oauth.service.token.OAuthTokenService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.chzzk.howmeet.infra.oauth.exception.param.OAuthParamErrorCode.INVALID_AUTHORIZATION_CODE;
import static org.chzzk.howmeet.infra.oauth.exception.param.OAuthParamErrorCode.INVALID_PROVIDER_NAME;

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
        if (isNullOrBlank(code)) {
            throw new OAuthParamException(INVALID_AUTHORIZATION_CODE);   // 예외 처리 커스텀 예정
        }
    }

    private void validateProviderName(final String providerName) {
        if (isNullOrBlank(providerName)) {
            throw new OAuthParamException(INVALID_PROVIDER_NAME);   // 예외 처리 커스텀 예정
        }
    }

    private boolean isNullOrBlank(final String value) {
        return value == null || value.isBlank();
    }
}
