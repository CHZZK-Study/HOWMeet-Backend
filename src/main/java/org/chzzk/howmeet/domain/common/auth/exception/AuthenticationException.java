package org.chzzk.howmeet.domain.common.auth.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends AuthException {
    public AuthenticationException(final AuthErrorCode errorCode) {
        super(errorCode, HttpStatus.UNAUTHORIZED);
    }
}
