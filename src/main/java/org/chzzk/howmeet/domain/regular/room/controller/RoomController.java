package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.annotation.Authenticated;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.annotation.RegularUser;
import org.chzzk.howmeet.domain.regular.room.dto.PageResponse;
import org.chzzk.howmeet.domain.regular.room.dto.RoomCreateResponse;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.service.RoomService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @RegularUser
    public ResponseEntity<?> createRoom(@RequestBody final RoomRequest roomRequest) {
        final RoomCreateResponse roomCreateResponse = roomService.createRoom(roomRequest);
        return ResponseEntity.ok(roomCreateResponse);
    }

    @PatchMapping("/{roomId}")
    @RegularUser
    public ResponseEntity<?> updateRoom(
            @PathVariable Long roomId,
            @RequestBody final RoomRequest roomRequest) {
        roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}")
    @RegularUser
    public ResponseEntity<?> getRoom(@PathVariable Long roomId, @Authenticated final AuthPrincipal authPrincipal) {
        final RoomResponse roomResponse = roomService.getRoom(roomId, authPrincipal);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/joined/{memberId}")
    @RegularUser
    public ResponseEntity<?> getJoinedRooms(
            @PathVariable Long memberId,
            @PageableDefault(size = 6) Pageable pageable) {

        PageResponse response = roomService.getJoinedRooms(memberId, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roomId}")
    @RegularUser
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok("Room successfully deleted");
    }
}
