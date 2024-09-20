package org.chzzk.howmeet.domain.regular.record.exception;


import org.chzzk.howmeet.domain.common.error.DomainException;

public class MSRecordException extends DomainException {

    public MSRecordException(final MSRecordErrorCode errorCode){
        super(errorCode);
    }
}
