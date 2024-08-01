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
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
import org.chzzk.howmeet.domain.temporary.auth.util.PasswordEncoder;
import org.chzzk.howmeet.global.util.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GuestService {
    private final GuestRepository guestRepository;
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

        final Guest guest = Guest.of(
                guestScheduleId,
                nickname,
                EncodedPassword.of(password, passwordEncoder)
        );

        return GuestSignupResponse.of(guestRepository.save(guest));
    }

    private void validateDuplicateNicknameInScheduleId(final Long guestScheduleId, final String nickname) {
        if (guestRepository.existsByGuestScheduleIdAndNickname(guestScheduleId, Nickname.from(nickname))) {
            throw new IllegalArgumentException();
        }
    }

    private Guest findGuestByNicknameAndScheduleId(final String nickname, final Long guestScheduleId) {
        return guestRepository.findByGuestScheduleIdAndNickname(guestScheduleId, Nickname.from(nickname))
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
