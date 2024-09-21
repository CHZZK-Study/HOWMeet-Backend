package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.*;
import org.chzzk.howmeet.domain.regular.room.service.RoomService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody final RoomRequest roomRequest) {
        final RoomCreateResponse roomCreateResponse = roomService.createRoom(roomRequest);
        return ResponseEntity.ok(roomCreateResponse);
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long roomId,
            @RequestBody final RoomRequest roomRequest) {
        roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable Long roomId) {
        final RoomResponse roomResponse = roomService.getRoom(roomId);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/joined/{memberId}")
    public ResponseEntity<?> getJoinedRooms(
            @PathVariable Long memberId,
            @PageableDefault(size = 6) Pageable pageable) {

        PageResponse response = roomService.getJoinedRooms(memberId, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok("Room successfully deleted");
    }
}
