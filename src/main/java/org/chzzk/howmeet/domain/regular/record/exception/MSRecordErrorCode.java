package org.chzzk.howmeet.domain.regular.record.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;


@Getter
public enum MSRecordErrorCode implements DomainErrorCode {
    DATE_INVALID_SELECT ("선택할 수 없는 날짜가 포함되었습니다.", HttpStatus.BAD_REQUEST),
    TIME_INVALID_SELECT ("선택할 수 없는 시간이 포함되었습니다.", HttpStatus.BAD_REQUEST),
    ROOM_LEADER_UNAUTHORIZED ("현 사용자는 방장이 아니므로 조회 권한이 없습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;
    MSRecordErrorCode(final String message, final HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
