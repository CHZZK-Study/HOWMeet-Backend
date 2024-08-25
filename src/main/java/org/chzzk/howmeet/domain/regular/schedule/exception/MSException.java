package org.chzzk.howmeet.domain.regular.schedule.exception;

public class MSException extends RuntimeException {
    private final MSErrorCode errorCode;

    public MSException(final MSErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
