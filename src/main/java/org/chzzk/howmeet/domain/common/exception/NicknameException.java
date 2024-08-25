package org.chzzk.howmeet.domain.common.exception;

public class NicknameException extends RuntimeException {
    private final NicknameErrorCode errorCode;

    public NicknameException(final NicknameErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
