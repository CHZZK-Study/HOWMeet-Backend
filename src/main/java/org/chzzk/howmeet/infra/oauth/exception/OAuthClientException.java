package org.chzzk.howmeet.infra.oauth.exception;

import lombok.Getter;
import org.chzzk.howmeet.infra.error.InfraException;
import org.springframework.http.HttpStatus;

@Getter
public class OAuthClientException extends InfraException {
    public OAuthClientException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
