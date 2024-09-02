package org.chzzk.howmeet.domain.regular.auth.dto.authorize.request;

import jakarta.validation.constraints.NotBlank;

public record MemberAuthorizeRequest(@NotBlank String providerName) {
}
