package org.chzzk.howmeet.infra.oauth.service.profile;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.dto.token.response.OAuthTokenResponse;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;
import org.chzzk.howmeet.infra.oauth.service.decorator.OAuthTimeoutDecorator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OAuthProfileService {
    private final OAuthTimeoutDecorator oAuthTimeoutDecorator;
    private final ProfileFailHandler profileFailHandler;
    private final WebClient webClient;

    public Mono<OAuthProfile> getProfile(final OAuthProvider oAuthProvider,
                                         final OAuthTokenResponse oAuthTokenResponse) {
        return oAuthTimeoutDecorator.decorate(getProfileByToken(oAuthProvider, oAuthTokenResponse.access_token()));
    }

    private Mono<OAuthProfile> getProfileByToken(final OAuthProvider oAuthProvider,
                                                 final String socialAccessToken) {
        return webClient.method(oAuthProvider.profileMethod())
                .uri(oAuthProvider.profileUrl())
                .headers(header -> header.setBearerAuth(socialAccessToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> profileFailHandler.handle4xxError(clientResponse, oAuthProvider))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> profileFailHandler.handle5xxError(clientResponse, oAuthProvider))
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
