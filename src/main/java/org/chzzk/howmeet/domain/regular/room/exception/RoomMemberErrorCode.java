package org.chzzk.howmeet.domain.regular.room.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomMemberErrorCode {
    ROOM_NOT_FOUND("룸을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROOM_MEMBER_CREATION_FAILED("룸 멤버 생성에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    RoomMemberErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}