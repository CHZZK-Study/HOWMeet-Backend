package org.chzzk.howmeet.mock.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.auth.controller.RefreshTokenCookieProvider;
import org.chzzk.howmeet.domain.regular.auth.dto.login.MemberLoginResult;
import org.chzzk.howmeet.domain.regular.auth.dto.login.request.MemberLoginRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.login.response.MemberLoginResponse;
import org.chzzk.howmeet.mock.auth.service.MockAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
public class MockAuthController {
    private final MockAuthService mockAuthService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    @PostMapping("/oauth/login")
    public ResponseEntity<?> memberLogin(@RequestBody @Valid final MemberLoginRequest memberLoginRequest) {
        final MemberLoginResult memberLoginResult = mockAuthService.login(memberLoginRequest);
        final ResponseCookie cookie = refreshTokenCookieProvider.createCookie(memberLoginResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(MemberLoginResponse.from(memberLoginResult));
    }
}
