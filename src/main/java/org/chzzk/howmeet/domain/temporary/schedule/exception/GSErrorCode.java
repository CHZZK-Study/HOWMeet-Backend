package org.chzzk.howmeet.domain.temporary.schedule.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GSErrorCode {
    SCHEDULE_NOT_FOUND("스케줄을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SCHEDULE_ALREADY_EXISTS("이미 존재하는 스케줄입니다.", HttpStatus.BAD_REQUEST),
    INVALID_SCHEDULE_DATE("스케줄 날짜가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_SCHEDULE_TIME("스케줄 시간이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    SCHEDULE_CREATION_FAILED("스케줄 생성에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    GSErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
