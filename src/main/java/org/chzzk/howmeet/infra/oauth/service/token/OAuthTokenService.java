package org.chzzk.howmeet.infra.oauth.service.token;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.dto.token.response.OAuthTokenResponse;
import org.chzzk.howmeet.infra.oauth.exception.token.OAuthTokenIssueException;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.service.decorator.OAuthTimeoutDecorator;
import org.chzzk.howmeet.infra.oauth.util.converter.OAuthParamConverter;
import org.h2.util.StringUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OAuthTokenService {
    private final OAuthParamConverter oAuthParamConverter;
    private final OAuthTimeoutDecorator oAuthTimeoutDecorator;
    private final TokenIssueFailHandler tokenIssueFailHandler;
    private final WebClient webClient;

    public Mono<OAuthTokenResponse> getToken(final OAuthProvider oAuthProvider, final String code) {
        return oAuthTimeoutDecorator.decorate(getTokenFromCode(oAuthProvider, code))
                .flatMap(response -> {
                    if (failedTokenIssue(response)) {
                        return Mono.error(OAuthTokenIssueException.createWhenResponseIsNullOrEmpty());
                    }

                    return Mono.just(response);
                });
    }

    private Mono<OAuthTokenResponse> getTokenFromCode(final OAuthProvider provider, final String code) {
        return webClient.method(provider.tokenMethod())
                .uri(provider.tokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(oAuthParamConverter.convertToTokenParams(provider, code)))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> tokenIssueFailHandler.handle4xxError(clientResponse, provider))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> tokenIssueFailHandler.handle5xxError(clientResponse, provider))
                .bodyToMono(OAuthTokenResponse.class);
    }

    private boolean failedTokenIssue(final OAuthTokenResponse oAuthTokenResponse) {
        return Objects.isNull(oAuthTokenResponse) || StringUtils.isNullOrEmpty(oAuthTokenResponse.access_token());
    }
}
