package org.chzzk.howmeet.domain.temporary.auth.service;

import org.chzzk.howmeet.RestDocsTest;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.auth.controller.GuestController;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.request.GuestLoginRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.response.GuestLoginResponse;
import org.chzzk.howmeet.domain.temporary.auth.dto.signup.request.GuestSignupRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.signup.response.GuestSignupResponse;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.fixture.GuestFixture.KIM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureRestDocs
class GuestServiceTest extends RestDocsTest {
    @Mock
    GuestRepository guestRepository;

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
    GuestSignupRequest guestSignupRequest = new GuestSignupRequest(guest.getGuestScheduleId(), nickname, password);
    GuestSignupResponse guestSignupResponse = GuestSignupResponse.of(guest);

    @Test
    @DisplayName("1회용 로그인")
    public void login() throws Exception {
        // given
        final GuestLoginResponse expect = guestLoginResponse;

        // when
        doNothing().when(passwordValidator)
                .validate(password);
        doReturn(true).when(passwordEncoder)
                .matches(password, encodedPassword.getValue());
        doReturn(Optional.of(guest)).when(guestRepository)
                .findByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());
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
    @DisplayName("1회용 로그인 시 닉네임이 잘못되면 예외 발생")
    public void loginWhenInvalidNickname() throws Exception {
        // when
        doNothing().when(passwordValidator)
                .validate(password);
        doReturn(Optional.empty()).when(guestRepository)
                .findByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());

        // then
        assertThatThrownBy(() -> guestService.login(guestLoginRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("1회용 로그인 시 비밀번호 검증 실패시 예외 발생")
    public void loginWhenInvalidPassword() throws Exception {
        // when
        doThrow(new RuntimeException()).when(passwordValidator)
                .validate(password);

        // then
        assertThatThrownBy(() -> guestService.login(guestLoginRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("1회용 로그인 시 비밀번호가 일치하지 않으면 예외 발생")
    public void loginWhenNotMatchedPassword() throws Exception {
        // when
        doNothing().when(passwordValidator)
                .validate(password);
        doReturn(Optional.of(guest)).when(guestRepository)
                .findByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());
        doReturn(false).when(passwordEncoder)
                .matches(password, encodedPassword.getValue());

        // then
        assertThatThrownBy(() -> guestService.login(guestLoginRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("1회용 회원가입")
    public void signup() throws Exception {
        // given
        final GuestSignupResponse expect = guestSignupResponse;

        // when
        doNothing().when(passwordValidator)
                .validate(password);
        doReturn(false).when(guestRepository)
                .existsByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());
        doReturn(encodedPassword.getValue()).when(passwordEncoder)
                .encode(password);
        doReturn(guest).when(guestRepository)
                .save(any());
        final GuestSignupResponse actual = guestService.signup(guestSignupRequest);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("1회용 로그인시 닉네임 중복시 예외 발생")
    public void signupWhenDuplicatedNickname() throws Exception {
        // when
        doReturn(true).when(guestRepository)
                .existsByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());

        // then
        assertThatThrownBy(() -> guestService.signup(guestSignupRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("1회용 로그인시 닉네임 검증 실패시 예외 발생")
    public void signupWhenInvalidNickname() throws Exception {
        // when
        doThrow(new RuntimeException()).when(guestRepository)
                .existsByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());

        // then
        assertThatThrownBy(() -> guestService.signup(guestSignupRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("1회용 로그인시 비밀번호 검증 실패시 예외 발생")
    public void signupWhenInvalidPassword() throws Exception {
        // when
        doThrow(new RuntimeException()).when(passwordValidator)
                .validate(password);

        // then
        assertThatThrownBy(() -> guestService.signup(guestSignupRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("1회용 로그인시 일정 ID가 잘못된 경우 예외 발생")
    public void signupWhenInvalidScheduleId() throws Exception {
        // when
        doReturn(true).when(guestRepository)
                .existsByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());

        // then
        assertThatThrownBy(() -> guestService.signup(guestSignupRequest))
                .isInstanceOf(RuntimeException.class);
    }
}