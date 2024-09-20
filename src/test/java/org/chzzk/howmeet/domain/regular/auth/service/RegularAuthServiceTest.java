package org.chzzk.howmeet.domain.regular.auth.service;

import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.auth.util.TokenProvider;
import org.chzzk.howmeet.domain.regular.auth.dto.authorize.request.MemberAuthorizeRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.authorize.response.MemberAuthorizeResponse;
import org.chzzk.howmeet.domain.regular.auth.dto.login.MemberLoginResult;
import org.chzzk.howmeet.domain.regular.auth.dto.login.request.MemberLoginRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.reissue.MemberReissueResult;
import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.chzzk.howmeet.domain.regular.auth.exception.RefreshTokenException;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfileFactory;
import org.chzzk.howmeet.infra.oauth.repository.InMemoryOAuthProviderRepository;
import org.chzzk.howmeet.infra.oauth.service.OAuthClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.regular.auth.exception.RefreshTokenErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static org.chzzk.howmeet.domain.regular.auth.exception.RefreshTokenErrorCode.REFRESH_TOKEN_NOT_MATCHED;
import static org.chzzk.howmeet.fixture.MemberFixture.KIM;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class RegularAuthServiceTest {
    @Mock
    InMemoryOAuthProviderRepository inMemoryOAuthProviderRepository;

    @Mock
    OAuthClient oAuthClient;

    @Mock
    OAuthResultHandler oAuthResultHandler;

    @Mock
    RefreshTokenCrudService refreshTokenCrudService;

    @Mock
    TokenProvider tokenProvider;

    @InjectMocks
    RegularAuthService regularAuthService;

    Member member = KIM.생성();
    String code = "authenticate_code";
    String accessToken = "accessToken";

    AuthPrincipal authPrincipal = AuthPrincipal.from(member);
    String refreshTokenValue = "refreshToken";
    Long expiration = 360_000L;
    RefreshToken refreshToken = RefreshToken.of(authPrincipal, refreshTokenValue, expiration);

    MemberLoginResult memberLoginResult = MemberLoginResult.of(accessToken, member, refreshToken);

    @ParameterizedTest
    @DisplayName("인가 코드")
    @ValueSource(strings = {"naver", "google", "kakao"})
    public void authorize(final String providerName) throws Exception {
        // given
        final MemberAuthorizeRequest memberAuthorizeRequest = new MemberAuthorizeRequest(providerName);
        final OAuthAuthorizePayload oAuthAuthorizePayload = new OAuthAuthorizePayload(HttpMethod.GET, URI.create("authorizeURL"));
        final OAuthProvider oAuthProvider = getOAuthProvider(providerName);
        final MemberAuthorizeResponse expect = MemberAuthorizeResponse.from(oAuthAuthorizePayload);

        // when
        doReturn(oAuthProvider).when(inMemoryOAuthProviderRepository)
                .findByProviderName(providerName);
        doReturn(oAuthAuthorizePayload).when(oAuthClient)
                .getAuthorizePayload(oAuthProvider);
        final MemberAuthorizeResponse actual = regularAuthService.authorize(memberAuthorizeRequest);

        // then
        assertThat(actual).isEqualTo(expect);
    }
    
    @ParameterizedTest
    @DisplayName("소셜 로그인")
    @ValueSource(strings = {"naver", "google", "kakao"})
    public void login(final String providerName) throws Exception {
        // given
        final MemberLoginRequest memberLoginRequest = new MemberLoginRequest(providerName, code);
        final OAuthProvider oAuthProvider = getOAuthProvider(providerName);
        final OAuthProfile oAuthProfile = getOAuthProfile(oAuthProvider);

        // when
        doReturn(oAuthProvider).when(inMemoryOAuthProviderRepository)
                .findByProviderName(providerName);
        doReturn(Mono.just(oAuthProfile)).when(oAuthClient)
                .getProfile(oAuthProvider, code);
        doReturn(member).when(oAuthResultHandler)
                .saveOrGet(oAuthProfile);
        doReturn(accessToken).when(tokenProvider)
                .createToken(authPrincipal);
        doReturn(refreshToken).when(refreshTokenCrudService)
                .save(authPrincipal);
        final MemberLoginResult actual = regularAuthService.login(memberLoginRequest);
        final MemberLoginResult expected = memberLoginResult;

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("소셜 로그인시 잘못된 인가 코드시 예외 발생")
    @ValueSource(strings = {"naver", "google", "kakao"})
    public void loginWhenInvalidAuthenticateCode(final String providerName) throws Exception {
        // given
        final MemberLoginRequest memberLoginRequest = new MemberLoginRequest(providerName, code);
        final OAuthProvider oAuthProvider = getOAuthProvider(providerName);

        // when
        doThrow(new RuntimeException()).when(oAuthClient)
                .getProfile(oAuthProvider, code);

        // then
        assertThatThrownBy(() -> regularAuthService.login(memberLoginRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("로그아웃")
    public void logout() throws Exception {
        // when
        doNothing().when(refreshTokenCrudService)
                .delete(authPrincipal, refreshTokenValue);

        // then
        assertThatCode(() -> regularAuthService.logout(authPrincipal, refreshTokenValue))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("로그아웃 시 리프레시 토큰이 잘못된 경우 예외 발생")
    public void logoutWhenInvalidRefreshToken() throws Exception {
        // when
        doThrow(new RefreshTokenException(REFRESH_TOKEN_NOT_FOUND)).when(refreshTokenCrudService)
                .delete(authPrincipal, refreshTokenValue);

        // then
        assertThatThrownBy(() -> regularAuthService.logout(authPrincipal, refreshTokenValue))
                .isInstanceOf(RefreshTokenException.class)
                .hasMessageContaining(REFRESH_TOKEN_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("로그아웃 시 엑세스 토큰 회원 정보와 리프레시 토큰 회원 정보가 일치하지 않는 경우 예외 발생")
    public void logoutWhenNotMatchedRefreshToken() throws Exception {
        // when
        doThrow(new RefreshTokenException(REFRESH_TOKEN_NOT_MATCHED)).when(refreshTokenCrudService)
                .delete(authPrincipal, refreshTokenValue);

        // then
        assertThatThrownBy(() -> regularAuthService.logout(authPrincipal, refreshTokenValue))
                .isInstanceOf(RefreshTokenException.class)
                .hasMessageContaining(REFRESH_TOKEN_NOT_MATCHED.getMessage());
    }

    @Test
    @DisplayName("토큰 재발급")
    public void reissue() throws Exception {
        // given
        final MemberReissueResult expect = new MemberReissueResult(accessToken, refreshTokenValue);

        // when
        doNothing().when(refreshTokenCrudService)
                .delete(authPrincipal, refreshTokenValue);
        doReturn(accessToken).when(tokenProvider)
                .createToken(authPrincipal);
        doReturn(refreshToken).when(refreshTokenCrudService)
                .save(authPrincipal);
        final MemberReissueResult actual = regularAuthService.reissue(authPrincipal, refreshTokenValue);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("토큰 재발급 시 리프레시 토큰이 잘못된 경우 예외 발생")
    public void reissueWhenInvalidRefreshToken() throws Exception {
        // given
        final MemberReissueResult expect = new MemberReissueResult(accessToken, refreshTokenValue);

        // when
        doNothing().when(refreshTokenCrudService)
                .delete(authPrincipal, refreshTokenValue);
        doReturn(accessToken).when(tokenProvider)
                .createToken(authPrincipal);
        doReturn(refreshToken).when(refreshTokenCrudService)
                .save(authPrincipal);
        final MemberReissueResult actual = regularAuthService.reissue(authPrincipal, refreshTokenValue);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("토큰 재발급 시 엑세스 토큰 회원 정보와 리프레시 토큰 회원 정보가 일치하지 않는 경우 예외 발생")
    public void reissueWhenNotMatchedRefreshToken() throws Exception {
        // when
        doThrow(new RefreshTokenException(REFRESH_TOKEN_NOT_MATCHED)).when(refreshTokenCrudService)
                .delete(authPrincipal, refreshTokenValue);

        // then
        assertThatThrownBy(() -> regularAuthService.reissue(authPrincipal, refreshTokenValue))
                .isInstanceOf(RefreshTokenException.class)
                .hasMessageContaining(REFRESH_TOKEN_NOT_MATCHED.getMessage());
    }

    private OAuthProvider getOAuthProvider(final String providerName) {
        return new OAuthProvider(
                providerName,
                "clientId",
                "clientSecret",
                "code",
                "authorizeUrl",
                HttpMethod.GET,
                List.of("profile", "image"),
                "redirectUrl",
                "grantType",
                HttpMethod.GET,
                "tokenUrl",
                HttpMethod.GET,
                "profileUrl"
        );
    }

    private OAuthProfile getOAuthProfile(final OAuthProvider oAuthProvider) {
        return OAuthProfileFactory.of(Collections.emptyMap(), oAuthProvider);
    }
}