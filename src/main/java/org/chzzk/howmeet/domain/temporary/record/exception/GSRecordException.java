package org.chzzk.howmeet.domain.temporary.record.exception;

public class GSRecordException extends RuntimeException{
    private final GSRecordErrorCode errorCode;

    public GSRecordException(final GSRecordErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
