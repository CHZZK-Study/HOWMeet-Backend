package org.chzzk.howmeet.domain.regular.schedule.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class MSException extends DomainException {
    public MSException(final MSErrorCode errorCode) {
        super(errorCode);
    }
}
