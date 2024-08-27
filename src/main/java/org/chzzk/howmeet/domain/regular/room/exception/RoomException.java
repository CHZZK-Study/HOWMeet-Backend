package org.chzzk.howmeet.domain.regular.room.exception;

public class RoomException extends RuntimeException {
    private final RoomErrorCode errorCode;

    public RoomException(final RoomErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
