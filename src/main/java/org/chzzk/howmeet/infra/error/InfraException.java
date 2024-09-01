package org.chzzk.howmeet.infra.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InfraException extends RuntimeException {
    private final HttpStatus status;

    public InfraException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }
}
