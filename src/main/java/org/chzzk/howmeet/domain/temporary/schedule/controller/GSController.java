package org.chzzk.howmeet.domain.temporary.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.temporary.auth.annotation.TemporaryUser;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSRequest;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse;
import org.chzzk.howmeet.domain.temporary.schedule.service.GSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/guest-schedule")
@RestController
public class GSController {
    private final GSService gsService;

    @PostMapping
    public ResponseEntity<?> createGuestSchedule(@RequestBody final GSRequest gsRequest) {
        final GSResponse gsResponse = gsService.createGuestSchedule(gsRequest);
        return ResponseEntity.created(URI.create("/guest-schedule/" + gsResponse.id()))
                .build();
    }

    @GetMapping("/{guestScheduleId}")
    public ResponseEntity<?> getGuestSchedule(@PathVariable Long guestScheduleId) {
        final GSResponse gsResponse = gsService.getGuestSchedule(guestScheduleId);
        return ResponseEntity.ok(gsResponse);
    }
}
