package org.chzzk.howmeet.domain.regular.member.exception;

public class MemberException extends RuntimeException {
    private final MemberErrorCode errorCode;

    public MemberException(final MemberErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
