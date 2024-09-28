package org.chzzk.howmeet.domain.regular.confirm.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum ConfirmErrorCode implements DomainErrorCode {
    CONFIRM_SCHEDULE_NOT_FOUND("일치하는 일정 결과를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    SCHEDULE_ALREADY_CONFIRMED("이미 확정된 일정입니다.", HttpStatus.CONFLICT),
    ROOM_LEADER_UNAUTHORIZED ("현 사용자는 일정 확정 권한이 없습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;

    ConfirmErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
