package org.chzzk.howmeet.domain.temporary.record.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GSRecordErrorCode {
    GS_NOT_FOUND("일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
    GSRecordErrorCode(final String message, final HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
