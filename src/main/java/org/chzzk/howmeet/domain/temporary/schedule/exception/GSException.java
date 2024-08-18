package org.chzzk.howmeet.domain.temporary.schedule.exception;

public class GSException extends RuntimeException {
    private final GSErrorCode gsErrorCode;

    public GSException(final GSErrorCode gsErrorCode) {
        super(gsErrorCode.getMessage());
        this.gsErrorCode = gsErrorCode;
    }
}