package org.chzzk.howmeet.domain.regular.schedule.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MSErrorCode {
    SCHEDULE_NOT_FOUND("요청하신 일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND("요청하신 방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SCHEDULE_CREATION_FAILED("일정을 생성하는 데 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    MSErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}