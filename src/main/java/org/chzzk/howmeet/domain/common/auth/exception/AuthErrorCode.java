package org.chzzk.howmeet.domain.common.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode {
    JWT_EXPIRED("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    JWT_NOT_FOUND("토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    JWT_INVALID("토큰이 JWT 형식이 아닙니다.", HttpStatus.UNAUTHORIZED),
    JWT_INVALID_SUBJECT("토큰내 Subject가 잘못되었습니다.", HttpStatus.UNAUTHORIZED),
    JWT_FORBIDDEN("토큰에 인가 권한이 없습니다.", HttpStatus.FORBIDDEN);
    private final String message;
    private final HttpStatus status;

    AuthErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
