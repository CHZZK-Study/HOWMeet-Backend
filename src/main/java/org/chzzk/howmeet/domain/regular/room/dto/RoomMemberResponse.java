package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;

public record RoomMemberResponse(Long id, Long memberId, Boolean isLeader) {
    public static RoomMemberResponse of(final RoomMember roomMember) {
        return new RoomMemberResponse(
                roomMember.getId(),
                roomMember.getMemberId(),
                roomMember.getIsLeader()
        );
    }
}
