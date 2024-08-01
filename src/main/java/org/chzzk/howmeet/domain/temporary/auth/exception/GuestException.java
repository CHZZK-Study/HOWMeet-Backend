package org.chzzk.howmeet.domain.temporary.auth.exception;

public class GuestException extends RuntimeException {
    private final GuestErrorCode errorCode;

    public GuestException(final GuestErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
