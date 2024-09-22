package org.chzzk.howmeet.infra.oauth.exception.param;

import lombok.Getter;

@Getter
public enum OAuthParamErrorCode {
    INVALID_AUTHORIZATION_CODE("인가 코드는 널이거나 공백일 수 없습니다"),
    INVALID_PROVIDER_NAME("소셜 이름을 널이거나 공백일 수 없습니다.");

    private final String message;

    OAuthParamErrorCode(final String message) {
        this.message = message;
    }
}
