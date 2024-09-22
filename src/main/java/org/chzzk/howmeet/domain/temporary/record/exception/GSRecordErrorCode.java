package org.chzzk.howmeet.domain.temporary.record.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum GSRecordErrorCode implements DomainErrorCode {
    DATE_INVALID_SELECT ("선택할 수 없는 날짜가 포함되었습니다.", HttpStatus.BAD_REQUEST),
    TIME_INVALID_SELECT ("선택할 수 없는 시간이 포함되었습니다.", HttpStatus.BAD_REQUEST);
    private final String message;
    private final HttpStatus status;
    GSRecordErrorCode(final String message, final HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
