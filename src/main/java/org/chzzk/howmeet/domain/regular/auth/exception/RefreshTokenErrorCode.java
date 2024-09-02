package org.chzzk.howmeet.domain.regular.auth.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum RefreshTokenErrorCode implements DomainErrorCode {
    REFRESH_TOKEN_NO_AUTHORITY("정기 회원이 아닙니다.", HttpStatus.BAD_REQUEST);
    private final String message;
    private final HttpStatus status;

    RefreshTokenErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
