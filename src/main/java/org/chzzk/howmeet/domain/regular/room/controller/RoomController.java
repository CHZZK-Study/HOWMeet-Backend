package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.*;
import org.chzzk.howmeet.domain.regular.room.service.RoomMemberService;
import org.chzzk.howmeet.domain.regular.room.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody final RoomRequest roomRequest) {
        final RoomResponse roomResponse = roomService.createRoom(roomRequest);
        return ResponseEntity.ok(Map.of("roomId", roomResponse.roomId()));
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long roomId,
            @RequestBody final RoomRequest roomRequest) {
        final RoomResponse roomResponse = roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable Long roomId) {
        final RoomResponse roomResponse = roomService.getRoom(roomId);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/joined/{memberId}")
    public ResponseEntity<List<RoomListResponse>> getJoinedRooms(@PathVariable Long memberId) {
        List<RoomListResponse> joinedRooms = roomService.getJoinedRooms(memberId);
        return ResponseEntity.ok(joinedRooms);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok("Room successfully deleted");
    }
}
