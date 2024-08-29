package org.chzzk.howmeet.infra.oauth.service;

import org.chzzk.howmeet.infra.oauth.exception.OAuthErrorResponseFactory;
import org.chzzk.howmeet.infra.oauth.exception.OAuthServerException;
import org.chzzk.howmeet.infra.oauth.exception.profile.OAuthProfileException;
import org.chzzk.howmeet.infra.oauth.exception.profile.response.OAuthProfileErrorResponse;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Component
public class ProfileFailHandler {
    public Mono<OAuthProfileException> handle4xxError(final ClientResponse clientResponse,
                                                      final OAuthProvider oAuthProvider) {
        return clientResponse.bodyToMono(OAuthErrorResponseFactory.getProfileResponseClassFrom(oAuthProvider.name()))
                .map(OAuthProfileException::new);
    }

    public Mono<OAuthServerException> handle5xxError(final ClientResponse clientResponse,
                                                      final OAuthProvider oAuthProvider) {
        return clientResponse.bodyToMono(OAuthErrorResponseFactory.getProfileResponseClassFrom(oAuthProvider.name()))
                .map(OAuthProfileErrorResponse::getMessage)
                .map(OAuthServerException::new);
    }
}
