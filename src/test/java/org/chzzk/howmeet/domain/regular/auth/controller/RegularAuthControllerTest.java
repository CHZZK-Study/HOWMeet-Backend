package org.chzzk.howmeet.domain.regular.auth.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.dto.authorize.request.MemberAuthorizeRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.authorize.response.MemberAuthorizeResponse;
import org.chzzk.howmeet.domain.regular.auth.dto.login.MemberLoginResult;
import org.chzzk.howmeet.domain.regular.auth.dto.login.request.MemberLoginRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.reissue.MemberReissueResult;
import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.chzzk.howmeet.domain.regular.auth.service.RegularAuthService;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.global.config.ControllerTest;
import org.chzzk.howmeet.global.interceptor.AuthenticationInterceptor;
import org.chzzk.howmeet.global.interceptor.MemberAuthorityInterceptor;
import org.chzzk.howmeet.global.resolver.AuthPrincipalResolver;
import org.chzzk.howmeet.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import org.chzzk.howmeet.infra.oauth.model.OAuthAdapter;
import org.chzzk.howmeet.infra.oauth.model.OAuthProperties;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;

import static org.chzzk.howmeet.fixture.MemberFixture.KIM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
class RegularAuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    OAuthProperties oAuthProperties;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RegularAuthService regularAuthService;

    @MockBean
    AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    MemberAuthorityInterceptor memberAuthorityInterceptor;

    @MockBean
    AuthPrincipalResolver authPrincipalResolver;

    @SpyBean
    RefreshTokenCookieProvider refreshTokenCookieProvider;

    Member member = KIM.생성(1L);
    AuthPrincipal authPrincipal = AuthPrincipal.from(member);
    String accessToken = "accessTokenValue";
    String refreshTokenValue = "refreshTokenValue";
    RefreshToken refreshToken = RefreshToken.of(authPrincipal, refreshTokenValue, 3000L);

    @BeforeEach
    public void setUp() throws Exception {
        doReturn(true).when(authenticationInterceptor).preHandle(any(), any(), any());
        doReturn(true).when(memberAuthorityInterceptor).preHandle(any(), any(), any());
        doReturn(true).when(authPrincipalResolver).supportsParameter(any());
        doReturn(authPrincipal).when(authPrincipalResolver).resolveArgument(any(), any(), any(), any());
    }

    @ParameterizedTest
    @DisplayName("인가 코드")
    @ValueSource(strings = {"kakao", "naver", "google"})
    public void authorize(final String providerName) throws Exception {
        // given
        final MemberAuthorizeRequest memberAuthorizeRequest = new MemberAuthorizeRequest(providerName);
        final OAuthProvider oAuthProvider = getOAuthProvider(providerName);
        final OAuthAuthorizePayload oAuthAuthorizePayload = getoAuthAuthorizePayload(oAuthProvider);
        final MemberAuthorizeResponse memberAuthorizeResponse = MemberAuthorizeResponse.from(oAuthAuthorizePayload);

        // when
        doReturn(memberAuthorizeResponse).when(regularAuthService).authorize(memberAuthorizeRequest);
        final ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/oauth/authorize?providerName=" + providerName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberAuthorizeRequest))
        );

        // then
        verify(regularAuthService).authorize(memberAuthorizeRequest);
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("인가 코드",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("providerName").description("소셜 이름")
                ),
                responseFields(
                        fieldWithPath("clientId").type(STRING)
                                .description(providerName + " 클라이언트 ID"),
                        fieldWithPath("scopes").type(ARRAY)
                                .description(providerName + " 허용된 리소스 범위"),
                        fieldWithPath("method").type(STRING)
                                .description(providerName + " 인가 코드 요청 메소드"),
                        fieldWithPath("url")
                                .type(STRING).description(providerName + " 인가 코드 요청 URL")
                )
        ));
    }

    @ParameterizedTest
    @DisplayName("소셜 로그인")
    @ValueSource(strings = {"kakao", "naver", "google"})
    public void login(final String providerName) throws Exception {
        // given
        final String code = "authorize_code";
        final MemberLoginRequest memberLoginRequest = new MemberLoginRequest(providerName, code);
        final MemberLoginResult memberLoginResult = MemberLoginResult.of(accessToken, member, refreshToken);

        // when
        doReturn(memberLoginResult).when(regularAuthService).login(memberLoginRequest);
        final ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberLoginRequest))
        );

        // then
        verify(regularAuthService).login(memberLoginRequest);
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(MockMvcRestDocumentationWrapper.document("소셜 로그인",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("providerName").type(STRING)
                                .description("소셜 이름"),
                        fieldWithPath("code").type(STRING)
                                .description("인가 코드")
                ),
                responseCookies(
                        cookieWithName("refreshToken").description("리프레시 토큰")
                ),
                responseFields(
                        fieldWithPath("accessToken").type(STRING)
                                .description("엑세스 토큰"),
                        fieldWithPath("memberId").type(NUMBER)
                                .description("회원 ID"),
                        fieldWithPath("nickname").type(STRING)
                                .description("닉네임")
               )
        ));
    }

    @Test
    @DisplayName("로그아웃")
    public void logout() throws Exception {
        // when
        doNothing().when(regularAuthService).logout(authPrincipal, refreshTokenValue);
        final ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .cookie(new Cookie("refreshToken", refreshTokenValue))
        );

        // then
        verify(regularAuthService).logout(authPrincipal, refreshTokenValue);
        result.andExpect(status().isNoContent());

        // restdocs
        result.andDo(MockMvcRestDocumentationWrapper.document("소셜 로그아웃",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestCookies(
                        cookieWithName("refreshToken").description("리프레시 토큰")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("엑세스 토큰")
                ),
                responseHeaders(
                        headerWithName(SET_COOKIE).description("만료된 리프레시 토큰")
                )
        ));
    }

    @Test
    @DisplayName("토큰 재발급")
    public void reissue() throws Exception {
        // given
        final MemberReissueResult memberReissueResult = MemberReissueResult.of(accessToken, refreshToken);

        // when
        doReturn(memberReissueResult).when(regularAuthService).reissue(authPrincipal, refreshTokenValue);
        final ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .cookie(new Cookie("refreshToken", refreshTokenValue))
        );

        // then
        verify(regularAuthService).reissue(authPrincipal, refreshTokenValue);
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(MockMvcRestDocumentationWrapper.document("토큰 재발급",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestCookies(
                        cookieWithName("refreshToken").description("리프레시 토큰")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("엑세스 토큰")
                ),
                responseCookies(
                        cookieWithName("refreshToken").description("새로 발급된 리프레시 토큰")
                ),
                responseFields(
                        fieldWithPath("accessToken").type(STRING).description("엑세스 토큰")
                )
        ));
    }

    private OAuthAuthorizePayload getoAuthAuthorizePayload(final OAuthProvider oAuthProvider) {
        return OAuthAuthorizePayload.of(oAuthProvider, URI.create(
                oAuthProvider.authorizeUrl() + "?client_id=CLIENT_ID&response_type=RESPONSE_TYPE&scope=SCOPE" +
                        "&redirect_uri=ENCODED_REDIRECT_URI")
        );
    }

    private OAuthProvider getOAuthProvider(final String providerName) {
        return OAuthAdapter.getOAuthProviders(oAuthProperties)
                .get(providerName);
    }
}