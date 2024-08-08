package org.chzzk.howmeet.domain.common.auth.exception;

public class AuthException extends RuntimeException {
    private final AuthErrorCode errorCode;

    public AuthException(final AuthErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
