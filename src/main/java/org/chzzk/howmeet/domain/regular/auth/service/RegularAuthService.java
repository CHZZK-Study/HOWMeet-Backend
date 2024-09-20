package org.chzzk.howmeet.domain.regular.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.dto.authorize.request.MemberAuthorizeRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.authorize.response.MemberAuthorizeResponse;
import org.chzzk.howmeet.domain.regular.auth.dto.login.request.MemberLoginRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.login.MemberLoginResult;
import org.chzzk.howmeet.domain.regular.auth.dto.reissue.MemberReissueResult;
import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.common.auth.util.TokenProvider;
import org.chzzk.howmeet.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.repository.InMemoryOAuthProviderRepository;
import org.chzzk.howmeet.infra.oauth.service.OAuthClient;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Service
public class RegularAuthService {
    private final InMemoryOAuthProviderRepository inMemoryOAuthProviderRepository;
    private final OAuthClient oAuthClient;
    private final OAuthResultHandler oauthResultHandler;
    private final RefreshTokenCrudService refreshTokenCrudService;
    private final TokenProvider tokenProvider;

    public MemberAuthorizeResponse authorize(final MemberAuthorizeRequest memberAuthorizeRequest) {
        final String providerName = memberAuthorizeRequest.providerName();
        final OAuthProvider oAuthProvider = inMemoryOAuthProviderRepository.findByProviderName(providerName);
        final OAuthAuthorizePayload oAuthAuthorizePayload = oAuthClient.getAuthorizePayload(oAuthProvider);
        return MemberAuthorizeResponse.from(oAuthAuthorizePayload);
    }

    public MemberLoginResult login(final MemberLoginRequest memberLoginRequest) {
        final OAuthProvider oAuthProvider = inMemoryOAuthProviderRepository.findByProviderName(memberLoginRequest.providerName());
        final Member member = oAuthClient.getProfile(oAuthProvider, memberLoginRequest.code())
                .publishOn(Schedulers.boundedElastic())
                .map(oauthResultHandler::saveOrGet)
                .block();

        final AuthPrincipal authPrincipal = AuthPrincipal.from(member);
        final String accessToken = tokenProvider.createToken(authPrincipal);
        final RefreshToken refreshToken = refreshTokenCrudService.save(authPrincipal);
        return MemberLoginResult.of(accessToken, member, refreshToken);
    }

    public void logout(final AuthPrincipal authPrincipal, final String refreshTokenValue) {
        refreshTokenCrudService.delete(authPrincipal, refreshTokenValue);
    }

    public MemberReissueResult reissue(final AuthPrincipal authPrincipal, final String refreshTokenValue) {
        refreshTokenCrudService.delete(authPrincipal, refreshTokenValue);
        final String accessToken = tokenProvider.createToken(authPrincipal);
        final RefreshToken refreshToken = refreshTokenCrudService.save(authPrincipal);
        return MemberReissueResult.of(accessToken, refreshToken);
    }
}
