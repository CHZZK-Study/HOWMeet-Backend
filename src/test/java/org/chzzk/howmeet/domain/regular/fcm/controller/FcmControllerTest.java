package org.chzzk.howmeet.domain.regular.fcm.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.fcm.dto.FcmTokenRequest;
import org.chzzk.howmeet.domain.regular.fcm.dto.VapidResponse;
import org.chzzk.howmeet.domain.regular.fcm.service.FcmService;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.fixture.MemberFixture;
import org.chzzk.howmeet.global.config.ControllerTest;
import org.chzzk.howmeet.global.interceptor.AuthenticationInterceptor;
import org.chzzk.howmeet.global.interceptor.MemberAuthorityInterceptor;
import org.chzzk.howmeet.global.resolver.AuthPrincipalResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ControllerTest
public class FcmControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FcmService fcmService;

    @MockBean
    private AuthPrincipalResolver authPrincipalResolver;

    @MockBean
    private AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    private MemberAuthorityInterceptor memberAuthorityInterceptor;

    Member member = MemberFixture.KIM.생성();
    AuthPrincipal authPrincipal = AuthPrincipal.from(member);

    String accessToken = "accessTokenValue";

    @BeforeEach
    public void setUp() throws Exception {
        doReturn(true).when(authenticationInterceptor).preHandle(any(), any(), any());
        doReturn(true).when(memberAuthorityInterceptor).preHandle(any(), any(), any());
        doReturn(true).when(authPrincipalResolver).supportsParameter(any());
        doReturn(authPrincipal).when(authPrincipalResolver).resolveArgument(any(), any(), any(), any());
    }

    @Test
    @DisplayName("FCM VAPID 키 조회 테스트")
    void getVapidKey() throws Exception {
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/fcm/vapid")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("fcm-vapid-key",
                preprocessRequest(prettyPrint()),
                responseFields(
                        fieldWithPath("key").type(STRING).description("FCM VAPID 공개 키 예시")
                )
        ));
    }

    @Test
    @DisplayName("FCM 토큰 저장 테스트")
    void saveFcmToken() throws Exception {

        Long roomId = 1L;
        Long msId = 1L;
        Member member = MemberFixture.KIM.생성();
        FcmTokenRequest request = new FcmTokenRequest("sampleFcmToken");
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, member.getNickname().getValue(), member.getRole());

        doNothing().when(fcmService).saveFcmToken(any(FcmTokenRequest.class), any(AuthPrincipal.class));

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/fcm/fcm-token")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(request))
                .principal(() -> String.valueOf(authPrincipal))
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("fcm-save-token",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("엑세스 토큰")
                ),
                requestFields(
                        fieldWithPath("token").type(STRING).description("저장할 FCM 토큰")
                )
        ));
    }
}
