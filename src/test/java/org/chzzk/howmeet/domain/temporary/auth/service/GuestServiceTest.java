package org.chzzk.howmeet.domain.temporary.auth.service;

import org.chzzk.howmeet.RestDocsTest;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.auth.controller.GuestController;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.request.GuestLoginRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.response.GuestLoginResponse;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.auth.exception.GuestException;
import org.chzzk.howmeet.domain.temporary.auth.util.PasswordEncoder;
import org.chzzk.howmeet.global.util.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.temporary.auth.exception.GuestErrorCode.INVALID_PASSWORD;
import static org.chzzk.howmeet.fixture.GuestFixture.KIM;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureRestDocs
class GuestServiceTest extends RestDocsTest {
    @Mock
    GuestFindService guestFindService;

    @Mock
    GuestSaveService guestSaveService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    PasswordValidator passwordValidator;

    @Mock
    TokenProvider tokenProvider;

    @InjectMocks
    GuestService guestService;

    @Override
    protected Object initializeController() {
        return new GuestController(guestService);
    }

    Guest guest = KIM.생성();
    EncodedPassword encodedPassword = guest.getPassword();
    String nickname = KIM.getNickname();
    String password = KIM.getPassword();
    String accessToken = "accessToken";
    GuestLoginRequest guestLoginRequest = new GuestLoginRequest(guest.getGuestScheduleId(), nickname, password);;
    GuestLoginResponse guestLoginResponse = GuestLoginResponse.of(accessToken, guest);

    @Test
    @DisplayName("1회용 로그인 (기존 회원)")
    public void login() throws Exception {
        // given
        final GuestLoginResponse expect = guestLoginResponse;

        // when
        doNothing().when(passwordValidator)
                .validate(password);
        doReturn(true).when(passwordEncoder)
                .matches(password, encodedPassword.getValue());
        doReturn(Optional.of(guest)).when(guestFindService)
                .find(guestLoginRequest.guestScheduleId(), guestLoginRequest.nickname());
        doReturn(accessToken).when(tokenProvider)
                .createToken(AuthPrincipal.from(guest));
        final GuestLoginResponse actual = guestService.login(guestLoginRequest);

        // then
        assertThat(actual).isEqualTo(expect);

        // restdocs
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestLoginRequest)))
                .andExpect(status().isOk())
                .andDo(document("1회용 로그인",
                        requestFields(
                                fieldWithPath("guestScheduleId").description("게스트 일정 ID"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("액세스 토큰"),
                                fieldWithPath("guestId").type(NUMBER).description("게스트 id"),
                                fieldWithPath("nickname").type(STRING).description("닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("1회용 로그인 시 비밀번호 검증 실패시 예외 발생")
    public void loginWhenInvalidPassword() throws Exception {
        // when
        doThrow(new GuestException(INVALID_PASSWORD)).when(passwordValidator)
                .validate(password);

        // then
        assertThatThrownBy(() -> guestService.login(guestLoginRequest))
                .isInstanceOf(GuestException.class)
                .hasMessageContaining(INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("1회용 로그인 시 비밀번호가 일치하지 않으면 예외 발생")
    public void loginWhenNotMatchedPassword() throws Exception {
        // when
        doNothing().when(passwordValidator)
                .validate(password);
        doReturn(Optional.of(guest)).when(guestFindService)
                .find(guestLoginRequest.guestScheduleId(), guestLoginRequest.nickname());
        doReturn(false).when(passwordEncoder)
                .matches(password, encodedPassword.getValue());

        // then
        assertThatThrownBy(() -> guestService.login(guestLoginRequest))
                .isInstanceOf(GuestException.class)
                .hasMessageContaining(INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("1회용 로그인 (신규 회원)")
    public void signup() throws Exception {
        // given
        final GuestLoginResponse expect = guestLoginResponse;

        // when
        doNothing().when(passwordValidator)
                .validate(password);
        doReturn(Optional.empty()).when(guestFindService)
                .find(guestLoginRequest.guestScheduleId(), guestLoginRequest.nickname());
        doReturn(encodedPassword.getValue()).when(passwordEncoder)
                .encode(password);
        doReturn(true).when(passwordEncoder)
                        .matches(guestLoginRequest.password(), encodedPassword.getValue());
        doReturn(accessToken).when(tokenProvider)
                        .createToken(AuthPrincipal.from(guest));
        doReturn(guest).when(guestSaveService)
                .save(guestLoginRequest.guestScheduleId(), guestLoginRequest.nickname(), encodedPassword);
        final GuestLoginResponse actual = guestService.login(guestLoginRequest);

        // then
        assertThat(actual).isEqualTo(expect);
    }
}