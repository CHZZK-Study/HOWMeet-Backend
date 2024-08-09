package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody final RoomRequest roomRequest) {
        final RoomResponse roomResponse = roomService.createRoom(roomRequest);
        return ResponseEntity.ok(roomResponse);
    }

    @PatchMapping("/{roomId}/edit")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long roomId,
            @RequestBody final RoomRequest roomRequest) {
        final RoomResponse roomResponse = roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.ok(roomResponse);
    }

    @PatchMapping("/{roomId}/members")
    public ResponseEntity<RoomResponse> updateRoomMembers(
            @PathVariable Long roomId,
            @RequestBody final List<RoomMemberRequest> roomMemberRequests) {
        final RoomResponse roomResponse = roomService.updateRoomMembers(roomId, roomMemberRequests);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable Long roomId) {
        final RoomResponse roomResponse = roomService.getRoom(roomId);
        return ResponseEntity.ok(roomResponse);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok("Room successfully deleted");
    }

    @DeleteMapping("/{roomId}/members/{roomMemberId}")
    public ResponseEntity<?> deleteRoomMember(@PathVariable Long roomId, @PathVariable Long roomMemberId) {
        roomService.deleteRoomMember(roomId, roomMemberId);
        return ResponseEntity.ok("RoomMember successfully deleted");
    }
}
