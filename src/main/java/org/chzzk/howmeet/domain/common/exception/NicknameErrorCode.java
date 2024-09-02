package org.chzzk.howmeet.domain.common.exception;

import lombok.Getter;
import org.chzzk.howmeet.domain.common.error.DomainErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum NicknameErrorCode implements DomainErrorCode {
    INVALID_NICKNAME("닉네임이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    NicknameErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
