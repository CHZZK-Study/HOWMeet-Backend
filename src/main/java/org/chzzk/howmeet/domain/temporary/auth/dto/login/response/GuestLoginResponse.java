package org.chzzk.howmeet.domain.temporary.auth.dto.login.response;

import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;

public record GuestLoginResponse(String accessToken, Long guestId, String nickname) {
    public static GuestLoginResponse of(final String accessToken, final Guest guest) {
        return new GuestLoginResponse(accessToken, guest.getGuestScheduleId(), guest.getNickname().getValue());
    }
}
