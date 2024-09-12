package org.chzzk.howmeet.domain.common.auth.exception;

import lombok.Getter;

@Getter
public enum AuthErrorCode {
    JWT_INVALID_OR_EXPIRED("토큰이 잘못되었거나 만료되었습니다."),
    JWT_NOT_FOUND("토큰을 찾을 수 없습니다."),
    JWT_INVALID_TYPE("토큰이 JWT 형식이 아닙니다."),
    JWT_INVALID_SUBJECT("토큰내 Subject가 잘못되었습니다."),
    JWT_FORBIDDEN("토큰에 인가 권한이 없습니다.");

    private final String message;

    AuthErrorCode(final String message) {
        this.message = message;
    }
}
