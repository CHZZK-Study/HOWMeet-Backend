package org.chzzk.howmeet.infra.oauth.service;

import org.chzzk.howmeet.infra.oauth.exception.OAuthErrorResponseFactory;
import org.chzzk.howmeet.infra.oauth.exception.OAuthServerException;
import org.chzzk.howmeet.infra.oauth.exception.token.OAuthTokenIssueException;
import org.chzzk.howmeet.infra.oauth.exception.token.response.OAuthTokenIssueErrorResponse;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Component
public class TokenIssueFailHandler {
    public Mono<OAuthTokenIssueException> handle4xxError(final ClientResponse clientResponse,
                                                         final OAuthProvider oAuthProvider) {
        return clientResponse.bodyToMono(OAuthErrorResponseFactory.getTokenIssueResponseClassFrom(oAuthProvider.name()))
                .map(OAuthTokenIssueException::new);
    }

    public Mono<OAuthServerException> handle5xxError(final ClientResponse clientResponse,
                                                      final OAuthProvider oAuthProvider) {
        return clientResponse.bodyToMono(OAuthErrorResponseFactory.getTokenIssueResponseClassFrom(oAuthProvider.name()))
                .map(OAuthTokenIssueErrorResponse::getMessage)
                .map(OAuthServerException::new);
    }
}
