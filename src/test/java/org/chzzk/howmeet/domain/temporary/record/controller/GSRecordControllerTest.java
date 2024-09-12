package org.chzzk.howmeet.domain.temporary.record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.record.dto.get.response.GSRecordGetResponse;
import org.chzzk.howmeet.domain.temporary.record.dto.post.request.GSRecordPostRequest;
import org.chzzk.howmeet.domain.temporary.record.service.GSRecordService;
import org.chzzk.howmeet.fixture.GSRecordFixture;
import org.chzzk.howmeet.fixture.GuestFixture;
import org.chzzk.howmeet.global.config.ControllerTest;
import org.chzzk.howmeet.global.resolver.AuthPrincipalResolver;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
public class GSRecordControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GSRecordService gsRecordService;

    @MockBean
    private AuthPrincipalResolver resolver;

    void setup() {
        createAuthPrincipal();
    }

    private AuthPrincipal createAuthPrincipal() {
        Guest guest = GuestFixture.KIM.생성();
        return new AuthPrincipal(1L, guest.getNickname().getValue(), guest.getRole());
    }

    @Test
    @DisplayName("비회원 일정 조율: 일정 생성 테스트")
    void createGSRecord() throws Exception {
        Guest guest = GuestFixture.KIM.생성();
        GSRecordPostRequest request = GSRecordFixture.createGSRecordPostRequestA();
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, guest.getNickname().getValue(), guest.getRole());

        doNothing().when(gsRecordService).postGSRecord(any(GSRecordPostRequest.class), any(AuthPrincipal.class));


        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/gs-record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(() -> String.valueOf(authPrincipal))
        );


        // then
        result.andExpect(status().isCreated());

        // restdocs
        result.andDo(document("비회원 일정 조율: 일정 생성",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("gsId").type(NUMBER).description("일정 ID"),
                        fieldWithPath("selectTime[]").type(ARRAY).description("선택한 시간 목록")
                )
        ));
    }

    @Test
    @DisplayName("비회원 일정 조율: 회원 일정 리스트 get요청")
    void getGSRecord() throws Exception {
        // given
        Long gsId = 1L;
        GSRecordGetResponse gsRecordGetResponse = GSRecordFixture.createGSRecordGetResponseA();

        given(gsRecordService.getGSRecord(eq(gsId)))
                .willReturn(gsRecordGetResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/gs-record/{gsId}", gsId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("비회원 일정 조율: 회원 일정 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("gsId").description("일정 ID")
                ),
                responseFields(
                        fieldWithPath("gsId").type(NUMBER).description("일정 ID"),
                        fieldWithPath("totalPersonnel").type(ARRAY).description("총 인원들의 닉네임 리스트"),
                        fieldWithPath("participatedPersonnel").type(ARRAY).description("참여 인원들의 닉네임 리스트"),
                        fieldWithPath("time[].selectTime").type(STRING).description("선택된 시간"),
                        fieldWithPath("time[].participantDetails.count").type(NUMBER).description("참여자 수"),
                        fieldWithPath("time[].participantDetails.nicknames").type(ARRAY).description("참여한 사람들의 닉네임")
                )
        ));
    }
}
