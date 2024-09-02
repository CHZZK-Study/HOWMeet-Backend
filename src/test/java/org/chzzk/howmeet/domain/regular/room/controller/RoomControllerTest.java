package org.chzzk.howmeet.domain.regular.room.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chzzk.howmeet.domain.regular.room.dto.*;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.service.RoomMemberService;
import org.chzzk.howmeet.domain.regular.room.service.RoomService;
import org.chzzk.howmeet.fixture.RoomFixture;
import org.chzzk.howmeet.fixture.RoomMemberFixture;
import org.chzzk.howmeet.global.config.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
public class RoomControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    private RoomMemberService roomMemberService;

    @Test
    @DisplayName("방 생성 테스트")
    void createRoom() throws Exception {
        // given
        RoomRequest roomRequest = RoomFixture.createRoomRequestA();
        RoomResponse roomResponse = RoomFixture.createRoomResponseA();

        given(roomService.createRoom(any(RoomRequest.class))).willReturn(roomResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequest))
        );

        // then
        result.andExpect(status().isCreated());

        // restdocs
        result.andDo(document("방 생성",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("name").type(STRING).description("방 이름"),
                        fieldWithPath("description").type(STRING).description("방 설명"),
                        fieldWithPath("msRequest.dates").type(ARRAY).description("일정 날짜 목록"),
                        fieldWithPath("msRequest.time.startTime").type(STRING).description("일정 시작 시간"),
                        fieldWithPath("msRequest.time.endTime").type(STRING).description("일정 종료 시간"),
                        fieldWithPath("msRequest.name.value").type(STRING).description("일정 이름"),
                        fieldWithPath("msRequest.roomId").type(NUMBER).description("방 ID").optional(),
                        fieldWithPath("leaderMemberId").type(NUMBER).description("리더 멤버 ID")
                )
        ));
    }

    @Test
    @DisplayName("방 업데이트 테스트")
    void updateRoom() throws Exception {
        // given
        RoomRequest roomRequest = RoomFixture.createRoomRequestB();
        RoomResponse roomResponse = RoomFixture.createRoomResponseB();

        given(roomService.updateRoom(any(Long.class), any(RoomRequest.class))).willReturn(roomResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.patch("/room/{roomId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequest))
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("방 업데이트",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("roomId").description("방 ID")
                ),
                requestFields(
                        fieldWithPath("name").type(STRING).description("방 이름"),
                        fieldWithPath("description").type(STRING).description("방 설명"),
                        fieldWithPath("msRequest.dates").type(ARRAY).description("일정 날짜 목록"),
                        fieldWithPath("msRequest.time.startTime").type(STRING).description("일정 시작 시간"),
                        fieldWithPath("msRequest.time.endTime").type(STRING).description("일정 종료 시간"),
                        fieldWithPath("msRequest.name.value").type(STRING).description("일정 이름"),
                        fieldWithPath("msRequest.roomId").type(NUMBER).description("방 ID").optional(),
                        fieldWithPath("leaderMemberId").type(NUMBER).description("리더 멤버 ID")
                ),
                responseFields(
                        fieldWithPath("roomId").type(NUMBER).description("방 ID"),
                        fieldWithPath("name").type(STRING).description("방 이름"),
                        fieldWithPath("description").type(STRING).description("방 설명"),
                        fieldWithPath("roomMembers").type(ARRAY).description("방 멤버 목록").optional(),
                        fieldWithPath("roomMembers[].id").type(NUMBER).description("방 멤버 ID").optional(),
                        fieldWithPath("roomMembers[].memberId").type(NUMBER).description("멤버 ID"),
                        fieldWithPath("roomMembers[].isLeader").type(BOOLEAN).description("리더 여부"),
                        fieldWithPath("schedules").type(ARRAY).description("진행 중인 스케줄 목록").optional(),
                        fieldWithPath("schedules[].id").type(NUMBER).description("스케줄 ID"),
                        fieldWithPath("schedules[].dates").type(ARRAY).description("스케줄 날짜 목록"),
                        fieldWithPath("schedules[].time.startTime").type(STRING).description("스케줄 시작 시간"),
                        fieldWithPath("schedules[].time.endTime").type(STRING).description("스케줄 종료 시간"),
                        fieldWithPath("schedules[].name.value").type(STRING).description("스케줄 이름"),
                        fieldWithPath("schedules[].status").type(STRING).description("스케줄 상태")
                )
        ));
    }

    @Test
    @DisplayName("방 멤버 업데이트 테스트")
    void updateRoomMembers() throws Exception {
        // given
        Room room = RoomFixture.createRoomA();
        List<RoomMemberRequest> roomMemberRequests = RoomMemberFixture.createRoomMemberRequests(room);
        List<RoomMemberResponse> roomMemberResponses = RoomMemberFixture.createRoomMemberResponses(room);

        given(roomMemberService.updateRoomMembers(any(Long.class), anyList())).willReturn(roomMemberResponses);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.patch("/room/{roomId}/members", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomMemberRequests))
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("방 멤버 업데이트",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("roomId").description("방 ID")
                ),
                requestFields(
                        fieldWithPath("[].memberId").type(NUMBER).description("멤버 ID"),
                        fieldWithPath("[].roomId").type(NUMBER).description("방 ID"),
                        fieldWithPath("[].isLeader").type(BOOLEAN).description("리더 여부")
                ),
                responseFields(
                        fieldWithPath("[].id").type(NUMBER).description("방 멤버 ID").optional(),
                        fieldWithPath("[].memberId").type(NUMBER).description("멤버 ID"),
                        fieldWithPath("[].isLeader").type(BOOLEAN).description("리더 여부")
                )
        ));
    }
    @Test
    @DisplayName("방 조회 테스트")
    void getRoom() throws Exception {
        // given
        RoomResponse roomResponse = RoomFixture.createRoomResponseA();

        given(roomService.getRoom(any(Long.class))).willReturn(roomResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/room/{roomId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("방 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("roomId").description("방 ID")
                ),
                responseFields(
                        fieldWithPath("roomId").type(NUMBER).description("방 ID"),
                        fieldWithPath("name").type(STRING).description("방 이름"),
                        fieldWithPath("description").type(STRING).description("방 설명"),
                        fieldWithPath("roomMembers").type(ARRAY).description("방 멤버 목록").optional(),
                        fieldWithPath("roomMembers[].id").type(NUMBER).description("방 멤버 ID").optional(),
                        fieldWithPath("roomMembers[].memberId").type(NUMBER).description("멤버 ID"),
                        fieldWithPath("roomMembers[].isLeader").type(BOOLEAN).description("리더 여부"),
                        fieldWithPath("ongoingSchedules").type(ARRAY).description("진행 중인 스케줄 목록").optional(),
                        fieldWithPath("ongoingSchedules[].id").type(NUMBER).description("스케줄 ID"),
                        fieldWithPath("ongoingSchedules[].dates").type(ARRAY).description("스케줄 날짜 목록"),
                        fieldWithPath("ongoingSchedules[].time.startTime").type(STRING).description("스케줄 시작 시간"),
                        fieldWithPath("ongoingSchedules[].time.endTime").type(STRING).description("스케줄 종료 시간"),
                        fieldWithPath("ongoingSchedules[].name.value").type(STRING).description("스케줄 이름"),
                        fieldWithPath("completedSchedules").type(ARRAY).description("완료된 스케줄 목록").optional(),
                        fieldWithPath("completedSchedules[].id").type(NUMBER).description("스케줄 ID"),
                        fieldWithPath("completedSchedules[].dates").type(ARRAY).description("스케줄 날짜 목록"),
                        fieldWithPath("completedSchedules[].time.startTime").type(STRING).description("스케줄 시작 시간"),
                        fieldWithPath("completedSchedules[].time.endTime").type(STRING).description("스케줄 종료 시간"),
                        fieldWithPath("completedSchedules[].name.value").type(STRING).description("스케줄 이름")
                )
        ));
    }

    @Test
    @DisplayName("회원이 참여한 방 목록 조회 테스트")
    void getJoinedRooms() throws Exception {
        // given
        Long memberId = 1L;
        List<RoomListResponse> joinedRooms = List.of(
                RoomFixture.createRoomListResponseA(),
                RoomFixture.createRoomListResponseB()
        );

        given(roomService.getJoinedRooms(memberId)).willReturn(joinedRooms);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/room/joined/{memberId}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("회원 참여 방 목록 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("memberId").description("회원 ID")
                ),
                responseFields(
                        fieldWithPath("[].roomId").type(NUMBER).description("방 ID"),
                        fieldWithPath("[].name").type(STRING).description("방 이름"),
                        fieldWithPath("[].memberCount").description("방 멤버 수"),
                        fieldWithPath("[].ongoingSchedules").type(ARRAY).description("진행 중인 스케줄 목록").optional(),
                        fieldWithPath("[].ongoingSchedules[].id").type(NUMBER).description("스케줄 ID"),
                        fieldWithPath("[].ongoingSchedules[].dates").type(ARRAY).description("스케줄 날짜 목록"),
                        fieldWithPath("[].ongoingSchedules[].time.startTime").type(STRING).description("스케줄 시작 시간"),
                        fieldWithPath("[].ongoingSchedules[].time.endTime").type(STRING).description("스케줄 종료 시간"),
                        fieldWithPath("[].ongoingSchedules[].name.value").type(STRING).description("스케줄 이름")
                )
        ));
    }

    @Test
    @DisplayName("방 삭제 테스트")
    void deleteRoom() throws Exception {
        // given
        willDoNothing().given(roomService).deleteRoom(any(Long.class));

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/room/{roomId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("방 삭제",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("roomId").description("방 ID")
                )
        ));
    }

    @Test
    @DisplayName("방 멤버 삭제 테스트")
    void deleteRoomMember() throws Exception {
        // given
        willDoNothing().given(roomService).deleteRoomMember(any(Long.class), any(Long.class));

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/room/{roomId}/members/{roomMemberId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("방 멤버 삭제",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("roomId").description("방 ID"),
                        parameterWithName("roomMemberId").description("방 멤버 ID")
                )
        ));
    }
}
