package org.chzzk.howmeet.infra.oauth.service.authorize;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AuthorizeUriProvider {
    public URI provideEntryUri(final OAuthProvider oAuthProvider, final MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.fromUriString(oAuthProvider.authorizeUrl())
                .queryParams(queryParams)
                .build(true)
                .toUri();
    }
}
