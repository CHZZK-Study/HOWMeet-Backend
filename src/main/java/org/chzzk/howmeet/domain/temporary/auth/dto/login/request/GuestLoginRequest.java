package org.chzzk.howmeet.domain.temporary.auth.dto.login.request;

public record GuestLoginRequest(Long guestScheduleId, String nickname, String password) {
}
