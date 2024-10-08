package org.chzzk.howmeet.domain.regular.fcm.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum FcmErrorCode implements DomainErrorCode {
    MEMBER_UNAUTHORIZED ("현 사용자는 회원 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    FCMTOKEN_INVALID_VALUE("유효하지 않은 토큰 값입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    FcmErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
