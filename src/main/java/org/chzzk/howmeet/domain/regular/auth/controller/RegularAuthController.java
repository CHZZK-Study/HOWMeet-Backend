package org.chzzk.howmeet.domain.regular.auth.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.auth.dto.login.request.MemberLoginRequest;
import org.chzzk.howmeet.domain.regular.auth.dto.login.response.MemberLoginResponse;
import org.chzzk.howmeet.domain.regular.auth.service.RegularAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/oauth")
@RequiredArgsConstructor
@RestController
public class RegularAuthController {
    private final RegularAuthService regularAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final MemberLoginRequest memberLoginRequest) {
        final MemberLoginResponse memberLoginResponse = regularAuthService.login(memberLoginRequest);
        return ResponseEntity.ok(memberLoginResponse);
    }
}
