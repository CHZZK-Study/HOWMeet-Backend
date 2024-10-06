package org.chzzk.howmeet.domain.regular.record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetResponse;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.service.MSRecordService;
import org.chzzk.howmeet.fixture.MSRecordFixture;
import org.chzzk.howmeet.fixture.MemberFixture;
import org.chzzk.howmeet.global.config.ControllerTest;
import org.chzzk.howmeet.global.interceptor.AuthenticationInterceptor;
import org.chzzk.howmeet.global.interceptor.MemberAuthorityInterceptor;
import org.chzzk.howmeet.global.resolver.AuthPrincipalResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
public class MSReordControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MSRecordService msRecordService;

    @MockBean
    private AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    private MemberAuthorityInterceptor memberAuthorityInterceptor;

    @MockBean
    private AuthPrincipalResolver authPrincipalResolver;

    private final Member member = MemberFixture.KIM.생성();

    private final AuthPrincipal authPrincipal = AuthPrincipal.from(member);
    private final String accessToken = "accessTokenValue";

    @BeforeEach
    public void setUp() throws Exception {
        doReturn(true).when(authenticationInterceptor).preHandle(any(), any(), any());
        doReturn(true).when(memberAuthorityInterceptor).preHandle(any(), any(), any());
        doReturn(true).when(authPrincipalResolver).supportsParameter(any());
        doReturn(authPrincipal).when(authPrincipalResolver).resolveArgument(any(), any(), any(), any());
    }

    @Test
    @DisplayName("회원 일정 조율: 일정 생성 테스트")
    void createMSRecord() throws Exception {
        Member member = MemberFixture.KIM.생성();
        MSRecordPostRequest request = MSRecordFixture.createMSRecordPostRequestA();
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, member.getNickname().getValue(), member.getRole());

        doNothing().when(msRecordService).postMSRecord(any(MSRecordPostRequest.class), any(AuthPrincipal.class));

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/ms-record")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(request))
                .principal(() -> String.valueOf(authPrincipal))
        );

        // then
        result.andExpect(status().isCreated());

        // restdocs
        result.andDo(document("회원 일정 조율: 일정 생성",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("엑세스 토큰")
                ),
                requestFields(
                        fieldWithPath("msId").type(NUMBER).description("일정 ID"),
                        fieldWithPath("selectTime[]").type(ARRAY).description("선택한 시간 목록")
                )
        ));
    }


    @Test
    @DisplayName("회원 일정 조율: 회원 일정 리스트 get요청")
    void getMSRecord() throws Exception {
        // given
        Long roomId = 1L;
        Long msId = 1L;
        Member member = MemberFixture.KIM.생성();
        MSRecordGetResponse msRecordGetResponse = MSRecordFixture.createMSRecordGetResponseA();
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, member.getNickname().getValue(), member.getRole());

        given(msRecordService.getMSRecord(eq(roomId), eq(msId)))
                .willReturn(msRecordGetResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/ms-record/{roomId}/{msId}", roomId, msId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .principal(() -> String.valueOf(authPrincipal))

        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("회원 일정 조율: 회원 일정 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("엑세스 토큰")
                ),
                pathParameters(
                        parameterWithName("roomId").description("방 ID"),
                        parameterWithName("msId").description("일정 ID")
                ),
                responseFields(
                        fieldWithPath("msId").type(NUMBER).description("일정 ID"),
                        fieldWithPath("roomName").type(STRING).description("방 이름"),
                        fieldWithPath("totalPersonnel").type(ARRAY).description("총 인원들의 닉네임 리스트"),
                        fieldWithPath("participatedPersonnel").type(ARRAY).description("참여 인원들의 닉네임 리스트"),
                        fieldWithPath("time[].selectTime").type(STRING).description("선택된 시간"),
                        fieldWithPath("time[].participantDetails.count").type(NUMBER).description("참여자 수"),
                        fieldWithPath("time[].participantDetails.nicknames").type(ARRAY).description("참여한 사람들의 닉네임")
                )
        ));
    }
}
