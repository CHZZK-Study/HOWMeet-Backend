package org.chzzk.howmeet.domain.temporary.auth.service;

import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.exception.NicknameException;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.request.GuestLoginRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.response.GuestLoginResponse;
import org.chzzk.howmeet.domain.temporary.auth.dto.signup.request.GuestSignupRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.signup.response.GuestSignupResponse;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.auth.exception.GuestException;
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
import org.chzzk.howmeet.domain.temporary.auth.util.PasswordEncoder;
import org.chzzk.howmeet.global.util.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.common.exception.NicknameErrorCode.INVALID_NICKNAME;
import static org.chzzk.howmeet.domain.temporary.auth.exception.GuestErrorCode.GUEST_ALREADY_EXIST;
import static org.chzzk.howmeet.domain.temporary.auth.exception.GuestErrorCode.GUEST_NOT_FOUND;
import static org.chzzk.howmeet.domain.temporary.auth.exception.GuestErrorCode.INVALID_PASSWORD;
import static org.chzzk.howmeet.fixture.GuestFixture.KIM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {
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
    }

    @Test
    @DisplayName("1회용 로그인 시 일치하는 회원이 없는 경우 예외 발생")
    public void loginWhenInvalidNickname() throws Exception {
        // when
        doNothing().when(passwordValidator)
                .validate(password);
        doReturn(Optional.empty()).when(guestRepository)
                .findByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());

        // then
        assertThatThrownBy(() -> guestService.login(guestLoginRequest))
                .isInstanceOf(GuestException.class)
                .hasMessageContaining(GUEST_NOT_FOUND.getMessage());
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
        doReturn(Optional.of(guest)).when(guestRepository)
                .findByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());
        doReturn(false).when(passwordEncoder)
                .matches(password, encodedPassword.getValue());

        // then
        assertThatThrownBy(() -> guestService.login(guestLoginRequest))
                .isInstanceOf(GuestException.class)
                .hasMessageContaining(INVALID_PASSWORD.getMessage());
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
    @DisplayName("1회용 회원가입시 닉네임 중복시 예외 발생")
    public void signupWhenDuplicatedNickname() throws Exception {
        // when
        doReturn(true).when(guestRepository)
                .existsByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());

        // then
        assertThatThrownBy(() -> guestService.signup(guestSignupRequest))
                .isInstanceOf(GuestException.class)
                .hasMessageContaining(GUEST_ALREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("1회용 회원가입시 비밀번호 검증 실패시 예외 발생")
    public void signupWhenInvalidPassword() throws Exception {
        // when
        doThrow(new GuestException(INVALID_PASSWORD)).when(passwordValidator)
                .validate(password);

        // then
        assertThatThrownBy(() -> guestService.signup(guestSignupRequest))
                .isInstanceOf(GuestException.class)
                .hasMessageContaining(INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("1회용 회원가입시 일정 ID가 잘못된 경우 예외 발생")
    public void signupWhenInvalidScheduleId() throws Exception {
        // when
        doReturn(true).when(guestRepository)
                .existsByGuestScheduleIdAndNickname(guest.getGuestScheduleId(), guest.getNickname());

        // then
        assertThatThrownBy(() -> guestService.signup(guestSignupRequest))
                .isInstanceOf(GuestException.class)
                .hasMessageContaining(GUEST_ALREADY_EXIST.getMessage());
    }
}