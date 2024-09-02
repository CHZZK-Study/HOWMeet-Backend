package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;

import java.util.List;

public record RoomListResponse (Long roomId, String name, String memberSummary, List<MSResponse> schedules) {

    public RoomListResponse(Long roomId, String name, String memberSummary, List<MSResponse> schedules) {
        this.roomId = roomId;
        this.name = name;
        this.memberSummary = memberSummary;
        this.schedules = schedules;
    }
}
