package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;

public record RoomMemberResponse(Long id, Long memberId, String nickname, Boolean isLeader) {
    public static RoomMemberResponse from(final RoomMember roomMember, final String nickname) {
        return new RoomMemberResponse(
                roomMember.getId(),
                roomMember.getMemberId(),
                nickname,
                roomMember.getIsLeader()
        );
    }
}
