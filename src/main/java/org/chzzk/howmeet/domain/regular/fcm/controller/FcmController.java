package org.chzzk.howmeet.domain.regular.fcm.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.annotation.Authenticated;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.annotation.RegularUser;
import org.chzzk.howmeet.domain.regular.fcm.dto.FcmTokenRequest;
import org.chzzk.howmeet.domain.regular.fcm.dto.VapidResponse;
import org.chzzk.howmeet.domain.regular.fcm.service.FcmService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/fcm")
@RestController
public class FcmController {

    @Value("${vapid.public.key}")
    private String vapidPublicKey;
    private final FcmService fcmService;

    @GetMapping("/vapid")
    public ResponseEntity<?> getVapidKey(){
        return ResponseEntity.ok().body(VapidResponse.of(vapidPublicKey));
    }

    @PostMapping("/fcm-token")
    @RegularUser
    public ResponseEntity<?> saveFcmToken(@RequestBody final FcmTokenRequest fcmTokenPostRequest, @Authenticated final AuthPrincipal authPrincipal){
        fcmService.saveFcmToken(fcmTokenPostRequest, authPrincipal);
        return ResponseEntity.ok().build();
    }
}
