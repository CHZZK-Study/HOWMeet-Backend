package org.chzzk.howmeet.infra.oauth.exception;

import org.chzzk.howmeet.infra.error.InfraException;
import org.springframework.http.HttpStatus;

public class OAuthServerException extends InfraException {
    public OAuthServerException(final String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
