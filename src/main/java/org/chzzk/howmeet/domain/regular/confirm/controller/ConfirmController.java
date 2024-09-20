package org.chzzk.howmeet.domain.regular.confirm.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.annotation.Authenticated;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.confirm.dto.ConfirmScheduleRequest;
import org.chzzk.howmeet.domain.regular.confirm.service.ConfirmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/confirm")
@RestController
public class ConfirmController {

    private final ConfirmService confirmService;

    @PostMapping()
    public ResponseEntity<?> postConfirmRecord(@RequestBody final ConfirmScheduleRequest confirmScheduleRequest,
            @Authenticated final AuthPrincipal authPrincipal) throws Exception {

        Long confirmScheduleId = confirmService.postConfirmSchedule(confirmScheduleRequest, authPrincipal);
        return ResponseEntity.created(URI.create("/confirm/" + confirmScheduleId)).build();
    }

    @GetMapping("/{confirmScheduleId}")
    public ResponseEntity<?> getConfirmRecord(@PathVariable final Long confirmScheduleId){
        return ResponseEntity.ok(confirmService.getConfirmSchedule(confirmScheduleId));
    }
}
