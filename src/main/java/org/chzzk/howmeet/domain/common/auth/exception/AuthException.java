package org.chzzk.howmeet.domain.common.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {
    private final HttpStatus status;

    public AuthException(final AuthErrorCode errorCode, final HttpStatus status) {
        super(errorCode.getMessage());
        this.status = status;
    }
}
