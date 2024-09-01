package org.chzzk.howmeet.domain.regular.room.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class RoomMemberException extends DomainException {
    public RoomMemberException(final RoomMemberErrorCode errorCode) {
        super(errorCode);
    }
}
