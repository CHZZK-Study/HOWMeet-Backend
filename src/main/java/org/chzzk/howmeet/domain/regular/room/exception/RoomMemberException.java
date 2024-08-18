package org.chzzk.howmeet.domain.regular.room.exception;

public class RoomMemberException extends RuntimeException {
    private final RoomMemberErrorCode errorCode;

    public RoomMemberException(final RoomMemberErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
