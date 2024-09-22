package org.chzzk.howmeet.domain.temporary.record.exception;


import org.chzzk.howmeet.domain.common.error.DomainException;

public class GSRecordException extends DomainException {

    public GSRecordException(final GSRecordErrorCode errorCode){
        super(errorCode);
    }
}
