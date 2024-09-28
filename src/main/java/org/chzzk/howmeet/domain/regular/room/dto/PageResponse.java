package org.chzzk.howmeet.domain.regular.room.dto;

import java.util.List;

public record PageResponse(
        int totalRoomCount,
        List<?> roomList,
        int currentPage,
        int totalPages,
        boolean hasNextPage
) {

    public static PageResponse of(int totalRoomCount,List<?> romList, int currentPage, int totalPages, boolean hasNextPage) {
        return new PageResponse(totalRoomCount, romList, currentPage, totalPages, hasNextPage);
    }
}
