package org.chzzk.howmeet.domain.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {
    private final HttpStatus status;

    public DomainException(final DomainErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }
}
