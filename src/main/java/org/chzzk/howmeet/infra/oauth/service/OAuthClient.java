package org.chzzk.howmeet.infra.oauth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.dto.token.request.OAuthTokenRequest;
import org.chzzk.howmeet.infra.oauth.dto.token.response.OAuthTokenResponse;
import org.chzzk.howmeet.infra.oauth.exception.token.OAuthTokenIssueException;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.util.MultiValueMapConverter;
import org.h2.util.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class OAuthClient {
    private final MultiValueMapConverter multiValueMapConverter;
    private final OAuthTimeoutHandler oAuthTimeoutHandler;
    private final ProfileFailHandler profileFailHandler;
    private final TokenIssueFailHandler tokenIssueFailHandler;
    private final WebClient webClient;

    public Mono<Map<String, Object>> getProfile(final OAuthProvider provider, final String code) {
        return getTokenApplyRetryAndTimeout(provider, code)
                .flatMap(response -> {
                    if (failedTokenIssue(response)) {
                        return Mono.error(OAuthTokenIssueException.createWhenResponseIsNullOrEmpty());
                    }

                    return getProfileApplyRetryAndTimeout(provider, response);
                });
    }

    private Mono<Map<String, Object>> getProfileFromToken(final OAuthProvider provider, final String socialAccessToken) {
        return webClient.method(provider.profileMethod())
                .uri(provider.profileUrl())
                .headers(header -> header.setBearerAuth(socialAccessToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> profileFailHandler.handle4xxError(clientResponse, provider))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> profileFailHandler.handle5xxError(clientResponse, provider))
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    private Mono<OAuthTokenResponse> issueToken(final OAuthProvider provider, final String code) {
        return webClient.method(provider.tokenMethod())
                .uri(provider.tokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(getIssueTokenParams(provider, code)))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> tokenIssueFailHandler.handle4xxError(clientResponse, provider))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> tokenIssueFailHandler.handle5xxError(clientResponse, provider))
                .bodyToMono(OAuthTokenResponse.class);
    }

    private boolean failedTokenIssue(final OAuthTokenResponse oAuthTokenResponse) {
        return Objects.isNull(oAuthTokenResponse) || StringUtils.isNullOrEmpty(oAuthTokenResponse.access_token());
    }

    private Mono<OAuthTokenResponse> getTokenApplyRetryAndTimeout(final OAuthProvider provider, final String code) {
        return oAuthTimeoutHandler.handle(() -> issueToken(provider, code));
    }

    private Mono<Map<String, Object>> getProfileApplyRetryAndTimeout(final OAuthProvider provider, final OAuthTokenResponse response) {
        return oAuthTimeoutHandler.handle(() -> getProfileFromToken(provider, response.access_token()));
    }

    private MultiValueMap<String, String> getIssueTokenParams(final OAuthProvider provider, final String code) {
        return multiValueMapConverter.convertFrom(OAuthTokenRequest.of(provider, code));
    }
}
