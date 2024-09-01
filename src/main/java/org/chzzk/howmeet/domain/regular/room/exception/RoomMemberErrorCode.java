package org.chzzk.howmeet.domain.regular.room.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomMemberErrorCode implements DomainErrorCode {
    ROOM_NOT_FOUND("요청하신 방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROOM_MEMBER_CREATION_FAILED("해당 방의 멤버 생성에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    RoomMemberErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}