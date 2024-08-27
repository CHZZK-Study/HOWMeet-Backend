package org.chzzk.howmeet.domain.regular.member.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.annotation.Authenticated;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.annotation.RegularUser;
import org.chzzk.howmeet.domain.regular.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/summary")
    @RegularUser
    public ResponseEntity<?> getSummary(final @Authenticated AuthPrincipal authPrincipal) {
        return ResponseEntity.ok(memberService.getSummary(authPrincipal));
    }
}
