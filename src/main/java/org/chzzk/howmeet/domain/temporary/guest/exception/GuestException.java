package org.chzzk.howmeet.domain.temporary.guest.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class GuestException extends DomainException {
    public GuestException(final GuestErrorCode errorCode) {
        super(errorCode);
    }
}
