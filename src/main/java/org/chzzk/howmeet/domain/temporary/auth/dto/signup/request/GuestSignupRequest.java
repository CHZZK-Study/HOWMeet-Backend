package org.chzzk.howmeet.domain.temporary.auth.dto.signup.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GuestSignupRequest(
        @NotNull @Positive Long guestScheduleId,
        @NotBlank String nickname,
        @NotBlank String password) {
}
