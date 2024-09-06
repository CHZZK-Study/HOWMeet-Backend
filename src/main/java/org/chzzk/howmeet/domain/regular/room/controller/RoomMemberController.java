package org.chzzk.howmeet.domain.regular.room.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.annotation.Authenticated;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.annotation.RegularUser;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberResponse;
import org.chzzk.howmeet.domain.regular.room.dto.get.response.RoomMemberGetResponse;
import org.chzzk.howmeet.domain.regular.room.service.RoomMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController("/room/{roomId}/members")
public class RoomMemberController {
    private final RoomMemberService roomMemberService;

    @GetMapping
    @RegularUser
    public ResponseEntity<?> get(@Authenticated final AuthPrincipal authPrincipal,
                                 @PathVariable("roomId") final Long roomId) {
        final RoomMemberGetResponse roomMemberGetResponse = roomMemberService.get(authPrincipal, roomId);
        return ResponseEntity.ok(roomMemberGetResponse);
    }

    @DeleteMapping("/{roomMemberId}")
    public ResponseEntity<?> deleteRoomMember(@PathVariable Long roomId, @PathVariable Long roomMemberId) {
        roomMemberService.deleteRoomMember(roomId, roomMemberId);
        return ResponseEntity.ok("RoomMember successfully deleted");
    }

    @PatchMapping
    public ResponseEntity<List<RoomMemberResponse>> updateRoomMembers(
            @PathVariable Long roomId,
            @RequestBody final List<RoomMemberRequest> roomMemberRequests) {
        List<RoomMemberResponse> roomMemberResponses = roomMemberService.updateRoomMembers(roomId, roomMemberRequests);
        return ResponseEntity.ok(roomMemberResponses);
    }
}
