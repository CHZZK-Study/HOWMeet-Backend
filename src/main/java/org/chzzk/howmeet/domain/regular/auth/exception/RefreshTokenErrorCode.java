package org.chzzk.howmeet.domain.regular.auth.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum RefreshTokenErrorCode implements DomainErrorCode {
    REFRESH_TOKEN_NO_AUTHORITY("회원이 아닙니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND("리프레시 토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REFRESH_TOKEN_NOT_MATCHED("엑세스 토큰의 회원 정보와 리프레시 토큰의 회원 정보가 일치하지 않습니다", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    RefreshTokenErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
