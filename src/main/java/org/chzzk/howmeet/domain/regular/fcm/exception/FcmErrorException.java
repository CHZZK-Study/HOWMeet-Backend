package org.chzzk.howmeet.domain.regular.fcm.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class FcmErrorException extends DomainException {
    public FcmErrorException(final FcmErrorCode errorCode) {
        super(errorCode);
    }

}
