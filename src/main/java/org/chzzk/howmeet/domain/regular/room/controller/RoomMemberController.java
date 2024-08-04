package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.RoomService;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/room-member")
@RestController
public class RoomMemberController {
    private final RoomMemberService roomMemberService;

    @PostMapping
    public ResponseEntity<?> createRoomMember(@RequestBody final RoomMemberRequest roomMemberRequest) {
        final RoomMemberResponse roomMemberResponse = roomMemberService.createRoomMember(roomMemberRequest);
        return ResponseEntity.ok(roomMemberResponse);
    }

    @PatchMapping("/{roomMemberId}/edit")
    public ResponseEntity<?> updateRoomMember(
            @PathVariable Long roomMemberId,
            @RequestBody final RoomMemberRequest roomMemberRequest) {
        final RoomMemberResponse roomMemberResponse = roomMemberService.updateRoomMember(roomMemberId, roomMemberRequest);
        return ResponseEntity.ok(roomMemberResponse);
    }

    @GetMapping("/{roomMemberId}")
    public ResponseEntity<?> getRoomMember(@PathVariable Long roomMemberId) {
        final RoomMemberResponse roomMemberResponse = roomMemberService.getRoomMember(roomMemberId);
        return ResponseEntity.ok(roomMemberResponse);
    }

    @DeleteMapping("/{roomMemberId}")
    public ResponseEntity<?> deleteRoomMember(@PathVariable Long roomMemberId) {
        roomMemberService.deleteRoomMember(roomMemberId);
        return ResponseEntity.noContent().build();
    }
}
