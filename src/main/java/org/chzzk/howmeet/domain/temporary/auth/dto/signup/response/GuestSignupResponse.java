package org.chzzk.howmeet.domain.temporary.auth.dto.signup.response;

import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;

public record GuestSignupResponse(Long guestId) {
    public static GuestSignupResponse of(final Guest guest) {
        return new GuestSignupResponse(guest.getId());
    }
}
