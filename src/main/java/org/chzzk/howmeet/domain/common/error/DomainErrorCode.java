package org.chzzk.howmeet.domain.common.error;

import org.springframework.http.HttpStatus;

public interface DomainErrorCode {
    HttpStatus getStatus();
    String getMessage();
}
