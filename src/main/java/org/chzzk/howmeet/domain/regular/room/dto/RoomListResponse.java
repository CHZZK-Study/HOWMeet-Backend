package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;

import java.util.List;

public record RoomListResponse (Long roomId, String name, String memberSummary, List<MSResponse> schedules, boolean hasNonParticipant) {

    public RoomListResponse(Long roomId, String name, String memberSummary, List<MSResponse> schedules, boolean hasNonParticipant) {
        this.roomId = roomId;
        this.name = name;
        this.memberSummary = memberSummary;
        this.schedules = schedules;
        this.hasNonParticipant = hasNonParticipant;
    }
}
