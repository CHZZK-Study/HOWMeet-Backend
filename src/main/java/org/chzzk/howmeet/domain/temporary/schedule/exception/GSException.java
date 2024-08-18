package org.chzzk.howmeet.domain.temporary.schedule.exception;

public class GSException extends RuntimeException {
    private final GSErrorCode errorCode;

    public GSException(final GSErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}