package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.schedule.dto.MemberScheduleRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MemberScheduleResponse;
import org.chzzk.howmeet.domain.regular.schedule.service.MemberScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/member-room")
@RestController
public class MemberRoomController {
    private final MemberRoomService memberRoomService;

    @PostMapping
    public ResponseEntity<?> createMemberRoom(@RequestBody final MemberRoomRequest memberRoomRequest) {
        final MemberRoomResponse memberRoomResponse = memberRoomService.createMemberRoom(memberRoomRequest);
        return ResponseEntity.ok(memberRoomResponse);
    }

    @PostMapping("/{memberRoomId}/edit")
    public ResponseEntity<?> updateMemberRoom(
            @PathVariable Long memberRoomId,
            @RequestBody final MemberRoomRequest memberRoomRequest) {
        final MemberRoomResponse memberRoomResponse = memberRoomService.createMemberRoom(memberRoomRequest);
        return ResponseEntity.ok(memberRoomResponse);
    }

    @GetMapping("/{memberRoomId}")
    public ResponseEntity<?> getMemberRoom(@PathVariable Long memberRoomId) {
        final MemberScheduleResponse memberScheduleResponse = memberRoomService.getMemberRoom(memberRoomId);
        return ResponseEntity.ok(memberScheduleResponse);
    }

    @DeleteMapping("/{memberRoomId}")
    public ResponseEntity<?> deleteMemberRoom(@PathVariable Long memberRoomId) {
        memberRoomService.deleteMemberRoom(memberRoomId);
        return ResponseEntity.noContent().build();
    }
}
