package org.chzzk.howmeet.infra.oauth.exception.param;

import org.chzzk.howmeet.infra.oauth.exception.OAuthClientException;

public class OAuthParamException extends OAuthClientException {
    public OAuthParamException(final OAuthParamErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
