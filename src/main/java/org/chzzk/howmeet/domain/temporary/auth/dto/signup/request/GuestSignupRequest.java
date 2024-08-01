package org.chzzk.howmeet.domain.temporary.auth.dto.signup.request;

public record GuestSignupRequest(Long guestScheduleId, String nickname, String password) {
}
