package org.chzzk.howmeet.infra.oauth.util.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.infra.oauth.dto.authorize.request.OAuthAuthorizeRequest;
import org.chzzk.howmeet.infra.oauth.dto.token.request.OAuthTokenRequest;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.util.mapper.OAuthObjectMapperFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class OAuthParamConverter {
    public MultiValueMap<String, String> convertToAuthorizeParams(final OAuthProvider oAuthProvider) {
        final ObjectMapper objectMapper = OAuthObjectMapperFactory.getFrom(oAuthProvider.name());
        return MultiValueMapConverter.convertFrom(objectMapper, OAuthAuthorizeRequest.from(oAuthProvider), TransformationStrategy.ENCODE);
    }

    public MultiValueMap<String, String> convertToTokenParams(final OAuthProvider oAuthProvider,
                                                              final String code) {
        final ObjectMapper objectMapper = OAuthObjectMapperFactory.getFrom(oAuthProvider.name());
        return MultiValueMapConverter.convertFrom(objectMapper, OAuthTokenRequest.of(oAuthProvider, code), TransformationStrategy.DECODE);
    }
}
