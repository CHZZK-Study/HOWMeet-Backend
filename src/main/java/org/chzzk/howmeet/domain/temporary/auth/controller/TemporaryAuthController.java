package org.chzzk.howmeet.domain.temporary.auth.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.request.GuestLoginRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.response.GuestLoginResponse;
import org.chzzk.howmeet.domain.temporary.auth.dto.signup.request.GuestSignupRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.signup.response.GuestSignupResponse;
import org.chzzk.howmeet.domain.temporary.auth.service.TemporaryAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class TemporaryAuthController {
    private final TemporaryAuthService temporaryAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final GuestLoginRequest guestLoginRequest) {
        final GuestLoginResponse guestLoginResponse = temporaryAuthService.login(guestLoginRequest);
        return ResponseEntity.ok(guestLoginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody final GuestSignupRequest guestSignupRequest) {
        final GuestSignupResponse guestSignupResponse = temporaryAuthService.signup(guestSignupRequest);
        return ResponseEntity.status(CREATED)
                .body(guestSignupResponse);
    }
}
