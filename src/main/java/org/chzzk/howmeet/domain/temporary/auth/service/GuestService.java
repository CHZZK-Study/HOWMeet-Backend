package org.chzzk.howmeet.domain.temporary.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.request.GuestLoginRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.response.GuestLoginResponse;
import org.chzzk.howmeet.domain.temporary.auth.dto.signup.request.GuestSignupRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.signup.response.GuestSignupResponse;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.auth.exception.GuestException;
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
import org.chzzk.howmeet.domain.temporary.auth.util.PasswordEncoder;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.chzzk.howmeet.global.util.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chzzk.howmeet.domain.temporary.auth.exception.GuestErrorCode.GUEST_ALREADY_EXIST;
import static org.chzzk.howmeet.domain.temporary.auth.exception.GuestErrorCode.GUEST_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GuestService {
    private final GuestRepository guestRepository;
    private final GSRepository gsRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final TokenProvider tokenProvider;

    public GuestLoginResponse login(final GuestLoginRequest guestLoginRequest) {
        passwordValidator.validate(guestLoginRequest.password());
        final Guest guest = findGuestByNicknameAndScheduleId(guestLoginRequest.nickname(), guestLoginRequest.guestScheduleId());
        guest.validatePassword(guestLoginRequest.password(), passwordEncoder);
        final AuthPrincipal authPrincipal = AuthPrincipal.from(guest);
        final String accessToken = tokenProvider.createToken(authPrincipal);
        return GuestLoginResponse.of(accessToken, guest);
    }

    @Transactional
    public GuestSignupResponse signup(final GuestSignupRequest guestSignupRequest) {
        final String password = guestSignupRequest.password();
        passwordValidator.validate(password);

        final Long guestScheduleId = guestSignupRequest.guestScheduleId();
        final String nickname = guestSignupRequest.nickname();
        validateDuplicateNicknameInScheduleId(guestScheduleId, nickname);
        validateGuestScheduleId(guestScheduleId);   // TODO 8/24 김민우 : SQL을 2개로 나눠서 Phantom Read 발생할까?

        final Guest guest = Guest.of(
                guestScheduleId,
                nickname,
                EncodedPassword.of(password, passwordEncoder)
        );

        return GuestSignupResponse.of(guestRepository.save(guest));
    }

    private void validateGuestScheduleId(final Long guestScheduleId) {
        if (!gsRepository.existsByGuestScheduleId(guestScheduleId)) {
            throw new IllegalArgumentException();   // TODO 8/24 김민우 : 비회원 일정 예외 처리 구현 후 수정 예정
        }
    }

    private void validateDuplicateNicknameInScheduleId(final Long guestScheduleId, final String nickname) {
        if (guestRepository.existsByGuestScheduleIdAndNickname(guestScheduleId, Nickname.from(nickname))) {
            throw new GuestException(GUEST_ALREADY_EXIST);
        }
    }

    private Guest findGuestByNicknameAndScheduleId(final String nickname, final Long guestScheduleId) {
        return guestRepository.findByGuestScheduleIdAndNickname(guestScheduleId, Nickname.from(nickname))
                .orElseThrow(() -> new GuestException(GUEST_NOT_FOUND));
    }
}
