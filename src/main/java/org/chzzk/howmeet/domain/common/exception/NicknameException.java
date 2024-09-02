package org.chzzk.howmeet.domain.common.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class NicknameException extends DomainException {
    public NicknameException(final NicknameErrorCode errorCode) {
        super(errorCode);
    }
}
