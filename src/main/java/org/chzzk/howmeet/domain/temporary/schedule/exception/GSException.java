package org.chzzk.howmeet.domain.temporary.schedule.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class GSException extends DomainException {
    public GSException(final GSErrorCode errorCode) {
        super(errorCode);
    }
}