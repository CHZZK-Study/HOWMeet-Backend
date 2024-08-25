package org.chzzk.howmeet.domain.regular.auth.service;

import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.dto.login.request.MemberLoginRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.login.response.MemberLoginResponse;
import org.chzzk.howmeet.domain.regular.auth.entity.Member;
import org.chzzk.howmeet.global.util.TokenProvider;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.repository.InMemoryOAuthProviderRepository;
import org.chzzk.howmeet.infra.oauth.service.OAuthClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.fixture.MemberFixture.KIM;
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
    TokenProvider tokenProvider;

    @InjectMocks
    RegularAuthService regularAuthService;

    Member member = KIM.생성();
    String code = "authenticate_code";
    String accessToken = "accessToken";
    MemberLoginResponse memberLoginResponse = MemberLoginResponse.of(accessToken, member);
    OAuthProvider oAuthProvider = getOAuthProvider();
    Map<String, Object> attribute = Collections.EMPTY_MAP;

    @ParameterizedTest
    @DisplayName("소셜 로그인")
    @ValueSource(strings = {"naver", "google", "kakao"})
    public void login(final String providerName) throws Exception {
        // given
        final MemberLoginRequest memberLoginRequest = new MemberLoginRequest(providerName, code);

        // when
        doReturn(oAuthProvider).when(inMemoryOAuthProviderRepository).findByProviderName(providerName);
        doReturn(Mono.just(attribute)).when(oAuthClient).getProfile(oAuthProvider, code);
        doReturn(member).when(oAuthResultHandler).saveOrGet(attribute, providerName);
        doReturn(accessToken).when(tokenProvider).createToken(AuthPrincipal.from(member));
        final MemberLoginResponse actual = regularAuthService.login(memberLoginRequest);
        final MemberLoginResponse expected = memberLoginResponse;

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("소셜 로그인시 잘못된 인가 코드시 예외 발생")
    @ValueSource(strings = {"naver", "google", "kakao"})
    public void loginWhenInvalidAuthenticateCode(final String providerName) throws Exception {
        // given
        final MemberLoginRequest memberLoginRequest = new MemberLoginRequest(providerName, code);

        // when
        doThrow(new RuntimeException()).when(oAuthClient).getProfile(oAuthProvider, code);

        // then
        assertThatThrownBy(() -> regularAuthService.login(memberLoginRequest))
                .isInstanceOf(RuntimeException.class);
    }

    private OAuthProvider getOAuthProvider() {
        return new OAuthProvider(
                "name",
                "clientId",
                "clientSecret",
                "redirectUrl",
                "grantType",
                HttpMethod.GET,
                "tokenUrl",
                HttpMethod.GET,
                "profileUrl"
        );
    }
}