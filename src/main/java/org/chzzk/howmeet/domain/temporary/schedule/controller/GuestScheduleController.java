package org.chzzk.howmeet.domain.temporary.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GuestScheduleRequest;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GuestScheduleResponse;
import org.chzzk.howmeet.domain.temporary.schedule.service.GuestScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/guest-schedule")
@RestController
public class GuestScheduleController {
    private final GuestScheduleService guestScheduleService;

    @PostMapping
    public ResponseEntity<?> createGuestSchedule(@RequestBody final GuestScheduleRequest guestScheduleRequest) {
        final GuestScheduleResponse guestScheduleResponse = guestScheduleService.createGuestSchedule(guestScheduleRequest);
        return ResponseEntity.ok(guestScheduleResponse);
    }

    @GetMapping("/{guestScheduleId}")
    public ResponseEntity<?> getGuestSchedule(@PathVariable Long guestScheduleId) {
        final GuestScheduleResponse guestScheduleResponse = guestScheduleService.getGuestSchedule(guestScheduleId);
        return ResponseEntity.ok(guestScheduleResponse);
    }

    @DeleteMapping("/{guestScheduleId}")
    public ResponseEntity<?> deleteGuestSchedule(@PathVariable Long guestScheduleId) {
        guestScheduleService.deleteGuestSchedule(guestScheduleId);
        return ResponseEntity.noContent().build();
    }
}
