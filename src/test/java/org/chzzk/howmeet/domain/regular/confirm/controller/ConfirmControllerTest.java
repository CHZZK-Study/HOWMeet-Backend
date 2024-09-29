package org.chzzk.howmeet.domain.regular.confirm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.confirm.dto.ConfirmScheduleResponse;
import org.chzzk.howmeet.domain.regular.confirm.dto.ConfirmScheduleRequest;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.confirm.service.ConfirmService;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.fixture.MemberFixture;
import org.chzzk.howmeet.fixture.RoomMemberFixture;
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
 import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
public class ConfirmControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfirmService confirmService;

    @MockBean
    private AuthPrincipalResolver authPrincipalResolver;

    @MockBean
    private AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    private MemberAuthorityInterceptor memberAuthorityInterceptor;

    Room room = new Room(RoomName.from("테스트 룸"));

     List<RoomMember> members = List.of(
             RoomMemberFixture.MEMBER_1.create(room),
             RoomMemberFixture.MEMBER_2.create(room),
             RoomMemberFixture.MEMBER_3.create(room)
     );

     List<String> dates = List.of("2023-01-01", "2023-01-02");
     ScheduleTime scheduleTime = ScheduleTime.of(LocalTime.of(9, 0), LocalTime.of(17,0));
     MemberSchedule memberSchedule = MemberSchedule.of(dates, scheduleTime, ScheduleName.from("테스트 일정"), room);
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
    @DisplayName("Confirm 일정 저장 테스트")
    void postConfirmSchedule() throws Exception {
        Long msId = 1L;

        List<LocalDateTime> time = Arrays.asList(
                LocalDateTime.of(2023, 1, 1, 10, 30),
                LocalDateTime.of(2023, 1, 1, 11, 0),
                LocalDateTime.of(2023, 1, 1, 11, 30)
        );

        List<String> participantPerson = Arrays.asList("지민", "오영", "세종");

        ConfirmScheduleRequest request = new ConfirmScheduleRequest(time, participantPerson);

        given(confirmService.postConfirmSchedule(anyLong(), any(ConfirmScheduleRequest.class), any(AuthPrincipal.class)))
                .willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/confirm/{msId}", msId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(request))
                .principal(() -> String.valueOf(authPrincipal))
        );

        // then
        result.andExpect(status().isCreated());

        // restdocs
        result.andDo(document("confirm-save-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("엑세스 토큰")
                ),
                pathParameters(
                    parameterWithName("msId").description("일정 ID")
                ),
                requestFields(
                        fieldWithPath("time[]").type(ARRAY).description("선택한 시간 목록 (LocalDateTime 리스트)"),
                        fieldWithPath("participantPerson[]").type(ARRAY).description("참가자 이름 목록")

                )
        ));
    }

    @Test
    @DisplayName("Confirm 일정 조회 테스트")
    void getConfirmSchedule() throws Exception {
        room.updateSchedules(List.of(memberSchedule));
        room.updateMembers(members);
        ReflectionTestUtils.setField(room, "id" ,1L);
        ReflectionTestUtils.setField(memberSchedule, "id" ,1L);


        // given
        Long roomId = 1L;
        Long msId = 1L;
        List<LocalDateTime> time = Arrays.asList(
                LocalDateTime.of(2023, 1, 1, 10, 30),
                LocalDateTime.of(2023, 1, 1, 11, 0),
                LocalDateTime.of(2023, 1, 1, 11, 30)
        );

        List<String> participantPerson = Arrays.asList("지민", "오영", "세종");
        ConfirmSchedule confirmSchedule = ConfirmSchedule.of(time, participantPerson, 1L);
        Nicknames totalPersonnel = Nicknames.create();
        totalPersonnel.add(Nickname.from("김민우"));
        totalPersonnel.add(Nickname.from("이수진"));

        Map<LocalDateTime, Nicknames> selectTimeMap = new HashMap<>();
        selectTimeMap.put(LocalDateTime.of(2023, 1, 1, 10, 30), totalPersonnel);
        List<SelectionDetail> selectTime = SelectionDetail.convertMapToSelectionDetailsList(selectTimeMap);

        ConfirmScheduleResponse response = ConfirmScheduleResponse.of(confirmSchedule, memberSchedule, totalPersonnel, selectTime);
        given(confirmService.getConfirmSchedule(roomId, msId)).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/confirm/{roomId}/{msId}", roomId, msId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("confirm-get-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("roomId").description("방 ID"),
                        parameterWithName("msId").description("일정 ID")
                ),
                responseFields(
                        fieldWithPath("roomName").type(STRING).description("방 이름"),
                        fieldWithPath("msName").type(STRING).description("일정 이름"),
                        fieldWithPath("confirmTime[]").type(ARRAY).description("선택된 시간 목록"),
                        fieldWithPath("participantPerson[]").type(ARRAY).description("참가자 이름 목록"),

                        fieldWithPath("totalPersonnel[]").type(ARRAY).description("해당 룸의 모든 참가자 이름 목록"),
                        fieldWithPath("time[]").type(ARRAY).description("해당 룸의 모든 참가자 이름 목록"),
                        fieldWithPath("time[].selectTime").type(STRING).description("선택된 시간"),
                        fieldWithPath("time[].participantDetails.count").type(NUMBER).description("해당 시간대를 선택한 멤버 수"),
                        fieldWithPath("time[].participantDetails.nicknames").type(ARRAY).description("해당 시간대를 선택한 멤버 닉네임")
                )
        ));
    }
}
