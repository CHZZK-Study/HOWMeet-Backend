//package org.chzzk.howmeet.domain.regular.room.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
//import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
//import org.chzzk.howmeet.domain.regular.room.dto.get.response.RoomMemberGetResponse;
//import org.chzzk.howmeet.domain.regular.room.entity.Room;
//import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
//import org.chzzk.howmeet.domain.regular.room.service.RoomMemberService;
//import org.chzzk.howmeet.global.config.ControllerTest;
//import org.chzzk.howmeet.global.interceptor.AuthenticationInterceptor;
//import org.chzzk.howmeet.global.interceptor.MemberAuthorityInterceptor;
//import org.chzzk.howmeet.global.resolver.AuthPrincipalResolver;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.util.List;
//
//import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
//import static org.chzzk.howmeet.fixture.MemberFixture.KIM;
//import static org.chzzk.howmeet.fixture.RoomFixture.createRoomA;
//import static org.chzzk.howmeet.fixture.RoomMemberFixture.MEMBER_1;
//import static org.chzzk.howmeet.fixture.RoomMemberFixture.createRoomMemberRequests;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.BDDMockito.willDoNothing;
//import static org.mockito.Mockito.doReturn;
//import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
//import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
//import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ControllerTest
//class RoomMemberControllerTest {
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RoomMemberService roomMemberService;
//
//    @MockBean
//    AuthenticationInterceptor authenticationInterceptor;
//
//    @MockBean
//    MemberAuthorityInterceptor memberAuthorityInterceptor;
//
//    @MockBean
//    AuthPrincipalResolver authPrincipalResolver;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        doReturn(true).when(authenticationInterceptor).preHandle(any(), any(), any());
//        doReturn(true).when(memberAuthorityInterceptor).preHandle(any(), any(), any());
//        doReturn(AuthPrincipal.from(KIM.생성())).when(authPrincipalResolver).resolveArgument(any(), any(), any(), any());
//    }
//
//    @Test
//    @DisplayName("방 멤버 조회 테스트")
//    public void getRoomMember() throws Exception {
//        // given
//        final Room roomA = createRoomA();
//        final RoomMember roomMember = MEMBER_1.create(roomA);
//        final RoomMemberGetResponse expect = RoomMemberGetResponse.from(roomMember);
//        doReturn(expect).when(roomMemberService).getRoomMember(any(), any());
//
//        // when
//        final ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/room/{roomId}/members", 1L)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        result.andExpect(status().isOk());
//
//        // restdocs
//        result.andDo(document("방 멤버 조회",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                pathParameters(
//                        parameterWithName("roomId").description("방 ID")
//                ),
//                requestHeaders(
//                        headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 토큰")
//                ),
//                responseFields(
//                        fieldWithPath("isLeader").type(BOOLEAN).description("방장 여부")
//                )
//        ));
//    }
//
//    @Test
//    @DisplayName("방 멤버 업데이트 테스트")
//    void updateRoomMembers() throws Exception {
//        // given
//        Room room = createRoomA();
//        List<RoomMemberRequest> roomMemberRequests = createRoomMemberRequests(room);
//
//        willDoNothing().given(roomMemberService).updateRoomMembers(any(Long.class), anyList());
//
//        // when
//        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.patch("/room/{roomId}/members", 1L)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(roomMemberRequests))
//        );
//
//        // then
//        result.andExpect(status().isOk());
//
//        // restdocs
//        result.andDo(document("방 멤버 업데이트",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                pathParameters(
//                        parameterWithName("roomId").description("방 ID")
//                ),
//                requestFields(
//                        fieldWithPath("[].memberId").type(NUMBER).description("멤버 ID"),
//                        fieldWithPath("[].isLeader").type(BOOLEAN).description("리더 여부")
//                )
//        ));
//    }
//
//    @Test
//    @DisplayName("방 멤버 삭제 테스트")
//    void deleteRoomMember() throws Exception {
//        // given
//        willDoNothing().given(roomMemberService).deleteRoomMember(any(Long.class), any(Long.class));
//
//        // when
//        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/room/{roomId}/members/{roomMemberId}", 1L, 1L)
//                .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        result.andExpect(status().isOk());
//
//        // restdocs
//        result.andDo(document("방 멤버 삭제",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                pathParameters(
//                        parameterWithName("roomId").description("방 ID"),
//                        parameterWithName("roomMemberId").description("방 멤버 ID")
//                )
//        ));
//    }
//}