package org.chzzk.howmeet.domain.regular.room.dto;

import java.util.List;

public record PageResponse(
        List<?> roomList,
        int currentPage,
        int totalPages,
        boolean hasNextPage
) {

    public static PageResponse of(List<?> romList, int currentPage, int totalPages, boolean hasNextPage) {
        return new PageResponse(romList, currentPage, totalPages, hasNextPage);
    }
}
