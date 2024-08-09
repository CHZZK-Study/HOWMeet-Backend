package org.chzzk.howmeet.domain.regular.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.service.MSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member-schedule")
@RestController
public class MSController {
    private final MSService msService;

    @PostMapping
    public ResponseEntity<?> createMemberSchedule(@RequestBody final MSRequest msRequest) {
        final MSResponse msResponse = msService.createMemberSchedule(msRequest);
        return ResponseEntity.ok(msResponse);
    }

    @GetMapping("/{memberScheduleId}")
    public ResponseEntity<?> getMemberSchedule(@PathVariable Long memberScheduleId) {
        final MSResponse msResponse = msService.getMemberSchedule(memberScheduleId);
        return ResponseEntity.ok(msResponse);
    }

    @DeleteMapping("/{memberScheduleId}")
    public ResponseEntity<?> deleteGuestSchedule(@PathVariable Long memberScheduleId) {
        msService.deleteMemberSchedule(memberScheduleId);
        return ResponseEntity.ok("Member schedule successfully deleted");
    }
}
