package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.*;
import org.chzzk.howmeet.domain.regular.room.service.RoomMemberService;
import org.chzzk.howmeet.domain.regular.room.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {
    private final RoomService roomService;
    private final RoomMemberService roomMemberService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody final RoomRequest roomRequest) {
        final RoomResponse roomResponse = roomService.createRoom(roomRequest);
        return ResponseEntity.created(URI.create("/room/" + roomResponse.roomId()))
                .body(roomResponse);
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long roomId,
            @RequestBody final RoomRequest roomRequest) {
        final RoomResponse roomResponse = roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.ok(roomResponse);
    }

    @PatchMapping("/{roomId}/members")
    public ResponseEntity<List<RoomMemberResponse>> updateRoomMembers(
            @PathVariable Long roomId,
            @RequestBody final List<RoomMemberRequest> roomMemberRequests) {
        List<RoomMemberResponse> roomMemberResponses = roomMemberService.updateRoomMembers(roomId, roomMemberRequests);
        return ResponseEntity.ok(roomMemberResponses);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable Long roomId) {
        final RoomResponse roomResponse = roomService.getRoom(roomId);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/joined")
    public ResponseEntity<List<RoomListResponse>> getJoinedRooms(@RequestParam Long memberId) {
        List<RoomListResponse> joinedRooms = roomService.getJoinedRooms(memberId);
        return ResponseEntity.ok(joinedRooms);
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
