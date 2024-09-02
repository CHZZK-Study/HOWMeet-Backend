package org.chzzk.howmeet.domain.regular.auth.dto.login.request;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank String providerName,
        @NotBlank String code) {
}
