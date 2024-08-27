package org.chzzk.howmeet.domain.regular.room.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomErrorCode {
    ROOM_NOT_FOUND("요청하신 방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROOM_MEMBER_NOT_FOUND("해당 방의 멤버를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_ROOM_MEMBER("해당 멤버가 지정된 방에 속하지 않습니다.", HttpStatus.BAD_REQUEST),
    ROOM_CREATION_FAILED("방 생성에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    RoomErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
