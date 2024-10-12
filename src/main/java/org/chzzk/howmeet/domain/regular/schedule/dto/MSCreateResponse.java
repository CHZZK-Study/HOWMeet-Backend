package org.chzzk.howmeet.domain.regular.schedule.dto;

import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

public record MSCreateResponse(Long roomId, Long msId) {
    public static MSCreateResponse from(final MemberSchedule memberSchedule) {
        return new MSCreateResponse(
                memberSchedule.getRoom().getId(),
                memberSchedule.getId()
        );
    }
}
