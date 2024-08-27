package org.chzzk.howmeet.infra.oauth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.util.MultiValueMapConverter;
import org.chzzk.howmeet.infra.oauth.dto.token.request.OAuthTokenRequest;
import org.chzzk.howmeet.infra.oauth.dto.token.response.OAuthTokenResponse;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuthClient {
    private final MultiValueMapConverter multiValueMapConverter;
    private final WebClient webClient;

    public Mono<Map<String, Object>> getProfile(final OAuthProvider provider, final String code) {
        return issueToken(provider, code)
                .flatMap(response -> getProfileFromToken(provider, response.access_token()));
    }

    private Mono<Map<String, Object>> getProfileFromToken(final OAuthProvider provider, final String socialAccessToken) {
        return webClient.method(provider.profileMethod())
                .uri(provider.profileUrl())
                .headers(header -> header.setBearerAuth(socialAccessToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    private Mono<OAuthTokenResponse> issueToken(final OAuthProvider provider, final String code) {
        return webClient.method(provider.tokenMethod())
                .uri(provider.tokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(getIssueTokenParams(provider, code)))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class);
    }

    private MultiValueMap<String, String> getIssueTokenParams(final OAuthProvider provider, final String code) {
        return multiValueMapConverter.convertFrom(OAuthTokenRequest.of(provider, code));
    }
}
