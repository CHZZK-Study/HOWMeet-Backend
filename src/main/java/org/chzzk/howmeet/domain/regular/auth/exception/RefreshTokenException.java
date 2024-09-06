package org.chzzk.howmeet.domain.regular.auth.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class RefreshTokenException extends DomainException {
    public RefreshTokenException(final RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }
}
