package org.chzzk.howmeet.domain.regular.room.dto;

public record RoomMemberRequest(Long memberId, Long roomId, Boolean isLeader) {
}