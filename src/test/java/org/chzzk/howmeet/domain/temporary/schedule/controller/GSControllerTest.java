//package org.chzzk.howmeet.domain.temporary.schedule.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.chzzk.howmeet.domain.temporary.schedule.dto.GSRequest;
//import org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse;
//import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
//import org.chzzk.howmeet.domain.temporary.schedule.service.GSService;
//import org.chzzk.howmeet.fixture.GSFixture;
//import org.chzzk.howmeet.global.config.ControllerTest;
//import org.chzzk.howmeet.global.config.WebConfig;
//import org.chzzk.howmeet.global.interceptor.AuthenticationInterceptor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willDoNothing;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.payload.JsonFieldType.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ControllerTest
//public class GSControllerTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private GSService gsService;
//
//    @Test
//    @DisplayName("비회원 일정 생성")
//    public void createGuestSchedule() throws Exception {
//        // given
//        final GSRequest gsRequest = GSFixture.createGSRequestA();
//        final GSResponse expectedResponse = GSFixture.createGSResponseA();
//
//        given(gsService.createGuestSchedule(any(GSRequest.class))).willReturn(expectedResponse);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/guest-schedule")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(gsRequest))
//        );
//
//        // then
//        resultActions.andExpect(status().isCreated());
//
//        // restdocs
//        resultActions.andDo(document("비회원 일정 생성",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                requestFields(
//                        fieldWithPath("dates").type(ARRAY).description("비회원 일정 날짜 목록"),
//                        fieldWithPath("time.startTime").type(STRING).description("비회원 일정 시작 시간"),
//                        fieldWithPath("time.endTime").type(STRING).description("비회원 일정 종료 시간"),
//                        fieldWithPath("name.value").type(STRING).description("비회원 일정 이름")
//                )
//        ));
//    }
//
//    @Test
//    @DisplayName("비회원 일정 조회")
//    public void getGuestSchedule() throws Exception {
//        // given
//        final GSResponse expectedResponse = GSFixture.createGSResponseA();
//        final GuestSchedule guestSchedule = GSFixture.createGuestScheduleA();
//        given(gsService.getGuestSchedule(guestSchedule.getId())).willReturn(expectedResponse);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/guest-schedule/{guestScheduleId}", guestSchedule.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isOk());
//
//        // restdocs
//        resultActions.andDo(document("비회원 일정 조회",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                pathParameters(
//                        parameterWithName("guestScheduleId").description("비회원 일정 ID")
//                ),
//                responseFields(
//                        fieldWithPath("id").type(NUMBER).description("비회원 일정 ID"),
//                        fieldWithPath("name.value").type(STRING).description("비회원 일정 이름"),
//                        fieldWithPath("dates").type(ARRAY).description("비회원 일정 날짜 목록"),
//                        fieldWithPath("time.startTime").type(STRING).description("비회원 일정 시작 시간"),
//                        fieldWithPath("time.endTime").type(STRING).description("비회원 일정 종료 시간"),
//                        fieldWithPath("status").type(STRING).description("비회원 일정 상태")
//                )
//        ));
//    }
//}