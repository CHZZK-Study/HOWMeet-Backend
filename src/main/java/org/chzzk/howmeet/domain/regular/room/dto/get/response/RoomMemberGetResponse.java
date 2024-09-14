package org.chzzk.howmeet.domain.regular.room.dto.get.response;

import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;

public record RoomMemberGetResponse(Boolean isLeader) {
    public static RoomMemberGetResponse from(final RoomMember roomMember) {
        return new RoomMemberGetResponse(roomMember.getIsLeader());
    }
}
