package org.chzzk.howmeet.domain.temporary.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.request.GuestLoginRequest;
import org.chzzk.howmeet.domain.temporary.auth.dto.login.response.GuestLoginResponse;
import org.chzzk.howmeet.domain.temporary.auth.service.TemporaryAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class TemporaryAuthController {
    private final TemporaryAuthService temporaryAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid final GuestLoginRequest guestLoginRequest) {
        final GuestLoginResponse guestLoginResponse = temporaryAuthService.login(guestLoginRequest);
        return ResponseEntity.ok(guestLoginResponse);
    }
}
