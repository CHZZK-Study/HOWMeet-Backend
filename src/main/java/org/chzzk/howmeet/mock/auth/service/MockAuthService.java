package org.chzzk.howmeet.mock.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.auth.util.TokenProvider;
import org.chzzk.howmeet.domain.regular.auth.dto.login.MemberLoginResult;
import org.chzzk.howmeet.domain.regular.auth.dto.login.request.MemberLoginRequest;
import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.chzzk.howmeet.domain.regular.auth.service.RefreshTokenCrudService;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockAuthService {
    private final MockMemberProvider mockMemberProvider;
    private final TokenProvider tokenProvider;
    private final RefreshTokenCrudService refreshTokenCrudService;

    public MemberLoginResult login(final MemberLoginRequest memberLoginRequest) {
        final Member member = mockMemberProvider.createMockMember();
        final AuthPrincipal authPrincipal = AuthPrincipal.from(member);
        final String accessToken = tokenProvider.createToken(authPrincipal);
        final RefreshToken refreshToken = refreshTokenCrudService.save(authPrincipal);
        return MemberLoginResult.of(accessToken, member, refreshToken);
    }
}
