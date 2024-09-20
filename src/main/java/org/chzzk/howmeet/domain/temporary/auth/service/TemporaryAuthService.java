package org.chzzk.howmeet.domain.temporary.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.request.GuestLoginRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.response.GuestLoginResponse;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.chzzk.howmeet.domain.temporary.guest.util.PasswordEncoder;
import org.chzzk.howmeet.domain.temporary.guest.service.GuestFindService;
import org.chzzk.howmeet.domain.temporary.guest.service.GuestSaveService;
import org.chzzk.howmeet.global.util.TokenProvider;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemporaryAuthService {
    private final GuestFindService guestFindService;
    private final GuestSaveService guestSaveService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final TokenProvider tokenProvider;

    public GuestLoginResponse login(final GuestLoginRequest guestLoginRequest) {
        passwordValidator.validate(guestLoginRequest.password());
        final Guest guest = saveOrGetGuest(guestLoginRequest);
        guest.validatePassword(guestLoginRequest.password(), passwordEncoder);
        final String accessToken = getAccessToken(guest);
        return GuestLoginResponse.of(accessToken, guest);
    }

    private Guest saveOrGetGuest(final GuestLoginRequest guestLoginRequest) {
        final Long guestScheduleId = guestLoginRequest.guestScheduleId();
        final String nickname = guestLoginRequest.nickname();
        return guestFindService.find(guestScheduleId, nickname)
                .orElseGet(() -> guestSaveService.save(guestScheduleId, nickname, EncodedPassword.of(guestLoginRequest.password(), passwordEncoder)));
    }

    private String getAccessToken(final Guest guest) {
        final AuthPrincipal authPrincipal = AuthPrincipal.from(guest);
        return tokenProvider.createToken(authPrincipal);
    }
}
