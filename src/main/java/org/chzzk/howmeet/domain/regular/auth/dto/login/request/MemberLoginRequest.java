package org.chzzk.howmeet.domain.regular.auth.dto.login.request;

public record MemberLoginRequest(String providerName, String code) {
}
