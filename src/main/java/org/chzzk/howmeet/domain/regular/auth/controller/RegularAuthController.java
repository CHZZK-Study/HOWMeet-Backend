package org.chzzk.howmeet.domain.regular.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.auth.dto.authorize.request.MemberAuthorizeRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.authorize.response.MemberAuthorizeResponse;
import org.chzzk.howmeet.domain.regular.auth.dto.login.request.MemberLoginRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.login.response.MemberLoginResponse;
import org.chzzk.howmeet.domain.regular.auth.service.RegularAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/oauth")
@RequiredArgsConstructor
@RestController
public class RegularAuthController {
    private final RegularAuthService regularAuthService;

    @GetMapping("/authorize")
    public ResponseEntity<?> authorize(@ModelAttribute @Valid final MemberAuthorizeRequest memberAuthorizeRequest) {
        final MemberAuthorizeResponse memberAuthorizeResponse = regularAuthService.authorize(memberAuthorizeRequest);
        return ResponseEntity.ok(memberAuthorizeResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid final MemberLoginRequest memberLoginRequest) {
        final MemberLoginResponse memberLoginResponse = regularAuthService.login(memberLoginRequest);
        return ResponseEntity.ok(memberLoginResponse);
    }
}
