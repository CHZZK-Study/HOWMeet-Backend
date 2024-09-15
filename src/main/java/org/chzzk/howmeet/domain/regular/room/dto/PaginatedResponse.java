package org.chzzk.howmeet.domain.regular.room.dto;

import java.util.List;

public record PaginatedResponse(
        List<RoomListResponse> roomList,
        int currentPage,
        int totalPages,
        boolean hasNextPage
) {

    public static PaginatedResponse of(List<RoomListResponse> romList, int currentPage, int totalPages, boolean hasNextPage) {
        return new PaginatedResponse(romList, currentPage, totalPages, hasNextPage);
    }
}
