//package org.chzzk.howmeet.domain.regular.schedule.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
//import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
//import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
//import org.chzzk.howmeet.domain.regular.schedule.service.MSService;
//import org.chzzk.howmeet.fixture.MSFixture;
//import org.chzzk.howmeet.fixture.RoomFixture;
//import org.chzzk.howmeet.global.config.ControllerTest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
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
//public class MSControllerTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MSService msService;
//
//    @BeforeEach
//    public void setUp() {
//        objectMapper.registerModule(new JavaTimeModule());
//    }
//
//    @Test
//    @DisplayName("회원 일정 생성")
//    public void createMemberSchedule() throws Exception {
//        // given
//        final MSRequest msRequest = MSFixture.createMSRequestA();
//        final MSResponse expectedResponse = MSFixture.createMSResponseA(RoomFixture.createRoomA());
//
//        given(msService.createMemberSchedule(any(Long.class), any(MSRequest.class))).willReturn(expectedResponse);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/room/{roomId}", 1L)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(msRequest))
//        );
//
//        // then
//        resultActions.andExpect(status().isCreated());
//
//        // restdocs
//        resultActions.andDo(document("회원 일정 생성",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                pathParameters(
//                        parameterWithName("roomId").description("룸 ID")
//                ),
//                requestFields(
//                        fieldWithPath("dates").type(ARRAY).description("회원 일정 날짜 목록"),
//                        fieldWithPath("time.startTime").type(STRING).description("회원 일정 시작 시간"),
//                        fieldWithPath("time.endTime").type(STRING).description("회원 일정 종료 시간"),
//                        fieldWithPath("name.value").type(STRING).description("회원 일정 이름")
//                )
//        ));
//    }
//
//    @Test
//    @DisplayName("회원 일정 조회")
//    public void getMemberSchedule() throws Exception {
//        // given
//        final MSResponse expectedResponse = MSFixture.createMSResponseA(RoomFixture.createRoomA());
//        final MemberSchedule memberSchedule = MSFixture.createMemberScheduleA(RoomFixture.createRoomA());
//        given(msService.getMemberSchedule(any(Long.class), any(Long.class))).willReturn(expectedResponse);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/room/{roomId}/{memberScheduleId}", 1L, memberSchedule.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isOk());
//
//        // restdocs
//        resultActions.andDo(document("회원 일정 조회",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                pathParameters(
//                        parameterWithName("roomId").description("룸 ID"),
//                        parameterWithName("memberScheduleId").description("회원 일정 ID")
//                ),
//                responseFields(
//                        fieldWithPath("id").type(NUMBER).description("회원 일정 ID"),
//                        fieldWithPath("name.value").type(STRING).description("회원 일정 이름"),
//                        fieldWithPath("dates").type(ARRAY).description("회원 일정 날짜 목록"),
//                        fieldWithPath("time.startTime").type(STRING).description("회원 일정 시작 시간"),
//                        fieldWithPath("time.endTime").type(STRING).description("회원 일정 종료 시간"),
//                        fieldWithPath("status").type(STRING).description("회원 일정 상태")
//                )
//        ));
//    }
//
//    @Test
//    @DisplayName("회원 일정 삭제")
//    public void deleteMemberSchedule() throws Exception {
//        // given
//        final MemberSchedule memberSchedule = MSFixture.createMemberScheduleA(RoomFixture.createRoomA());
//        willDoNothing().given(msService).deleteMemberSchedule(any(Long.class), any(Long.class));
//
//        // when
//        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/room/{roomId}/{memberScheduleId}", 1L, memberSchedule.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isOk());
//
//        // restdocs
//        resultActions.andDo(document("회원 일정 삭제",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                pathParameters(
//                        parameterWithName("roomId").description("룸 ID"),
//                        parameterWithName("memberScheduleId").description("회원 일정 ID")
//                )
//        ));
//    }
//}