package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{memberRoomId}/edit")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long roomId,
            @RequestBody final RoomRequest roomRequest) {
        final RoomResponse roomResponse = roomService.createMemberRoom(roomId, roomRequest);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/{memberRoomId}")
    public ResponseEntity<?> getMemberRoom(@PathVariable Long roomId) {
        final RoomResponse roomResponse = roomService.getRoom(roomId);
        return ResponseEntity.ok(roomResponse);
    }

    @DeleteMapping("/{memberRoomId}")
    public ResponseEntity<?> deleteMemberRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
