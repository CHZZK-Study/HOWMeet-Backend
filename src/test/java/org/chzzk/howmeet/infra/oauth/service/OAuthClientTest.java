package org.chzzk.howmeet.infra.oauth.service;

import org.chzzk.howmeet.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import org.chzzk.howmeet.infra.oauth.dto.token.response.OAuthTokenResponse;
import org.chzzk.howmeet.infra.oauth.exception.param.OAuthParamException;
import org.chzzk.howmeet.infra.oauth.fixture.OAuthProviderFixture;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfileFactory;
import org.chzzk.howmeet.infra.oauth.repository.InMemoryOAuthProviderRepository;
import org.chzzk.howmeet.infra.oauth.service.authorize.OAuthAuthorizeService;
import org.chzzk.howmeet.infra.oauth.service.profile.OAuthProfileService;
import org.chzzk.howmeet.infra.oauth.service.token.OAuthTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.infra.oauth.exception.param.OAuthParamErrorCode.INVALID_AUTHORIZATION_CODE;
import static org.chzzk.howmeet.infra.oauth.exception.param.OAuthParamErrorCode.INVALID_PROVIDER_NAME;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OAuthClientTest {
    @Mock
    InMemoryOAuthProviderRepository inMemoryOAuthProviderRepository;

    @Mock
    OAuthAuthorizeService oAuthAuthorizeService;

    @Mock
    OAuthTokenService oAuthTokenService;

    @Mock
    OAuthProfileService oAuthProfileService;

    @InjectMocks
    OAuthClient oAuthClient;

    String code = "code";
    OAuthTokenResponse oAuthTokenResponse = new OAuthTokenResponse("token_type", "access_token", 3000, "refresh_token", 3000);

    @ParameterizedTest
    @DisplayName("인가 코드")
    @ValueSource(strings = {"kakao", "naver", "google"})
    public void getAuthorizePayload(final String providerName) throws Exception {
        // given
        final OAuthProvider oAuthProvider = OAuthProviderFixture.getFrom(providerName);
        final OAuthAuthorizePayload expect = OAuthAuthorizePayload.of(oAuthProvider, URI.create("authorizeEntryUrl"));

        // when
        doReturn(oAuthProvider).when(inMemoryOAuthProviderRepository)
                .findByProviderName(providerName);
        doReturn(expect).when(oAuthAuthorizeService)
                .getAuthorizePayload(oAuthProvider);
        final OAuthAuthorizePayload actual = oAuthClient.getAuthorizePayload(providerName);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @ParameterizedTest
    @DisplayName("인가 코드 발급시 소셜 이름이 공백이나 널이면 예외 발생")
    @NullSource
    @ValueSource(strings = {"", " "})
    public void getAuthorizePayloadWhenInvalidProviderName(final String providerName) throws Exception {
        assertThatThrownBy(() -> oAuthClient.getAuthorizePayload(providerName))
                .isInstanceOf(OAuthParamException.class)
                .hasMessageContaining(INVALID_PROVIDER_NAME.getMessage());
    }

    @ParameterizedTest
    @DisplayName("프로필 조회")
    @ValueSource(strings = {"kakao", "naver", "google"})
    public void getProfile(final String providerName) throws Exception {
        // given
        final OAuthProvider oAuthProvider = OAuthProviderFixture.getFrom(providerName);
        final OAuthProfile oAuthProfile = getOAuthProfile(oAuthProvider);

        // when
        doReturn(oAuthProvider).when(inMemoryOAuthProviderRepository)
                .findByProviderName(providerName);
        doReturn(Mono.just(oAuthTokenResponse)).when(oAuthTokenService)
                .getToken(oAuthProvider, code);
        doReturn(Mono.just(oAuthProfile)).when(oAuthProfileService)
                .getProfile(oAuthProvider, oAuthTokenResponse);

        final Mono<OAuthProfile> actual = oAuthClient.getProfile(providerName, code);

        // then
        assertThat(actual.block()).isEqualTo(oAuthProfile);
    }

    @ParameterizedTest
    @DisplayName("프로필 조회시 소셜 이름이 공백이나 널이면 예외 발생")
    @NullSource
    @ValueSource(strings = {"", " "})
    public void getProfileWhenInvalidProviderName(final String providerName) throws Exception {
        final String code = "code";
        assertThatThrownBy(() -> oAuthClient.getProfile(providerName, code))
                .isInstanceOf(OAuthParamException.class)
                .hasMessageContaining(INVALID_PROVIDER_NAME.getMessage());
    }

    @ParameterizedTest
    @DisplayName("프로필 조회시 인가 코드가 공백이나 널이면 예외 발생")
    @NullSource
    @ValueSource(strings = {"", " "})
    public void getProfileWhenInvalidCode(final String code) throws Exception {
        final String providerName = "naver";
        assertThatThrownBy(() -> oAuthClient.getProfile(providerName, code))
                .isInstanceOf(OAuthParamException.class)
                .hasMessageContaining(INVALID_AUTHORIZATION_CODE.getMessage());
    }

    private OAuthProfile getOAuthProfile(final OAuthProvider oAuthProvider) {
        return OAuthProfileFactory.of(Collections.emptyMap(), oAuthProvider);
    }
}