package org.chzzk.howmeet.domain.regular.confirm.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;
public class ConfirmException extends DomainException{

    public ConfirmException(final ConfirmErrorCode errorCode) {
        super(errorCode);
    }

}

