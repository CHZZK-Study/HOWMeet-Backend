package org.chzzk.howmeet.domain.common.auth.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends AuthException {
    public AuthorizationException(final AuthErrorCode errorCode) {
        super(errorCode, HttpStatus.FORBIDDEN);
    }
}
