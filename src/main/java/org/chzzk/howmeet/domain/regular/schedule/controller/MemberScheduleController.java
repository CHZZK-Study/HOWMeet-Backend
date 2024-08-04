package org.chzzk.howmeet.domain.regular.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.schedule.dto.MemberScheduleRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MemberScheduleResponse;
import org.chzzk.howmeet.domain.regular.schedule.service.MemberScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member-schedule")
@RestController
public class MemberScheduleController {
    private final MemberScheduleService memberScheduleService;

    @PostMapping
    public ResponseEntity<?> createMemberSchedule(@RequestBody final MemberScheduleRequest memberScheduleRequest) {
        final MemberScheduleResponse memberScheduleResponse = memberScheduleService.createMemberSchedule(memberScheduleRequest);
        return ResponseEntity.ok(memberScheduleResponse);
    }

    @GetMapping("/{memberScheduleId}")
    public ResponseEntity<?> getMemberSchedule(@PathVariable Long memberScheduleId) {
        final MemberScheduleResponse memberScheduleResponse = memberScheduleService.getMemberSchedule(memberScheduleId);
        return ResponseEntity.ok(memberScheduleResponse);
    }

    @DeleteMapping("/{memberScheduleId}")
    public ResponseEntity<?> deleteGuestSchedule(@PathVariable Long memberScheduleId) {
        memberScheduleService.deleteMemberSchedule(memberScheduleId);
        return ResponseEntity.noContent().build();
    }
}
