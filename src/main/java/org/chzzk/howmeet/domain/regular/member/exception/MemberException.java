package org.chzzk.howmeet.domain.regular.member.exception;

import org.chzzk.howmeet.domain.common.error.DomainException;

public class MemberException extends DomainException {
    public MemberException(final MemberErrorCode errorCode) {
        super(errorCode);
    }
}
