package org.chzzk.howmeet.infra.oauth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.dto.authorize.request.OAuthAuthorizeRequest;
import org.chzzk.howmeet.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import org.chzzk.howmeet.infra.oauth.dto.token.request.OAuthTokenRequest;
import org.chzzk.howmeet.infra.oauth.dto.token.response.OAuthTokenResponse;
import org.chzzk.howmeet.infra.oauth.exception.token.OAuthTokenIssueException;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfileFactory;
import org.chzzk.howmeet.infra.oauth.util.MultiValueMapConverter;
import org.h2.util.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class OAuthClient {
    private final MultiValueMapConverter multiValueMapConverter;
    private final OAuthTimeoutDecorator oAuthTimeoutDecorator;
    private final ProfileFailHandler profileFailHandler;
    private final TokenIssueFailHandler tokenIssueFailHandler;
    private final WebClient webClient;

    public OAuthAuthorizePayload getAuthorizePayload(final OAuthProvider oAuthProvider) {
        final OAuthAuthorizeRequest oAuthAuthorizeRequest = OAuthAuthorizeRequest.from(oAuthProvider);
        final URI uri = getAuthorizeEntryUri(oAuthProvider, getAuthorizeParams(oAuthAuthorizeRequest));
        return OAuthAuthorizePayload.of(oAuthProvider, uri);
    }

    public Mono<OAuthProfile> getProfile(final OAuthProvider provider, final String code) {
        return oAuthTimeoutDecorator.decorate(issueToken(provider, code))
                .flatMap(response -> {
                    if (failedTokenIssue(response)) {
                        return Mono.error(OAuthTokenIssueException.createWhenResponseIsNullOrEmpty());
                    }

                    return oAuthTimeoutDecorator.decorate(getProfileFromToken(provider, response.access_token()))
                            .map(attributes -> OAuthProfileFactory.of(attributes, provider));
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

    private MultiValueMap<String, String> getAuthorizeParams(final OAuthAuthorizeRequest oAuthAuthorizeRequest) {
        return multiValueMapConverter.convertFrom(oAuthAuthorizeRequest);
    }

    private URI getAuthorizeEntryUri(final OAuthProvider oAuthProvider, final MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.fromUriString(oAuthProvider.authorizeUrl())
                .queryParams(queryParams)
                .build(true)
                .toUri();
    }

    private boolean failedTokenIssue(final OAuthTokenResponse oAuthTokenResponse) {
        return Objects.isNull(oAuthTokenResponse) || StringUtils.isNullOrEmpty(oAuthTokenResponse.access_token());
    }

    private MultiValueMap<String, String> getIssueTokenParams(final OAuthProvider provider, final String code) {
        return multiValueMapConverter.convertFrom(OAuthTokenRequest.of(provider, code));
    }
}
