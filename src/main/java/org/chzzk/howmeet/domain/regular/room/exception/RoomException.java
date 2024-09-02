package org.chzzk.howmeet.domain.regular.room.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class RoomException extends DomainException {
    public RoomException(final RoomErrorCode errorCode) {
        super(errorCode);
    }
}
