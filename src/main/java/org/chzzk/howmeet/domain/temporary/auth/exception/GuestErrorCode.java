package org.chzzk.howmeet.domain.temporary.auth.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum GuestErrorCode implements DomainErrorCode {
    GUEST_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    GUEST_ALREADY_EXIST("이미 존재하는 회원입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("비밀번호가 올바르지 않습니다", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    GuestErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
