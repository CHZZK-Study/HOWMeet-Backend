package org.chzzk.howmeet.domain.temporary.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.request.GuestLoginRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.response.GuestLoginResponse;
import org.chzzk.howmeet.domain.temporary.auth.service.TemporaryAuthService;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.chzzk.howmeet.global.config.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.chzzk.howmeet.fixture.GuestFixture.KIM;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
class TemporaryAuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TemporaryAuthService temporaryAuthService;

    String accessToken = "accessToken";
    Long guestScheduleId = 1L;
    String nickname = KIM.getNickname();
    String password = KIM.getPassword();
    Guest guest = KIM.생성(1L, guestScheduleId);

    @Test
    @DisplayName("1회용 회원 로그인")
    public void login() throws Exception {
        // given
        final GuestLoginRequest guestLoginRequest = new GuestLoginRequest(guestScheduleId, nickname, password);
        final GuestLoginResponse guestLoginResponse = GuestLoginResponse.of(accessToken, guest);

        // when
        doReturn(guestLoginResponse).when(temporaryAuthService)
                .login(guestLoginRequest);
        final ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guestLoginRequest))
        );

        // then
        result.andExpect(status().isOk());

        // restdocs
        result.andDo(document("1회용 회원 로그인",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("guestScheduleId").type(NUMBER).description("1회용 회원 일정 ID"),
                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                        fieldWithPath("password").type(STRING).description("비밀번호")
                ),
                responseFields(
                        fieldWithPath("accessToken").type(STRING).description("엑세스 토큰"),
                        fieldWithPath("guestId").type(NUMBER).description("1회용 회원 ID"),
                        fieldWithPath("nickname").type(STRING).description("닉네임")
                )
        ));
    }
}