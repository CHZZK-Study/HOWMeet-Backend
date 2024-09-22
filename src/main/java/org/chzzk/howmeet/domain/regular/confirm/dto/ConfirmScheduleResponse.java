package org.chzzk.howmeet.domain.regular.confirm.dto;

import java.time.LocalDateTime;
import java.util.List;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;

public record ConfirmScheduleResponse(RoomName roomName, String msName, List<LocalDateTime> time, List<String> participantPerson, Integer totalMemberCount, List<SelectTimeCount> timeTableData) {

    public static ConfirmScheduleResponse of(final ConfirmSchedule confirmSchedule, final Integer totalMemberCount, final List<SelectTimeCount> timeTableData) {
        return new ConfirmScheduleResponse(
                confirmSchedule.getMs().getRoom().getName(),
                confirmSchedule.getMs().getName().getValue(),
                confirmSchedule.getTime(),
                confirmSchedule.getParticipantName(),
                totalMemberCount,
                timeTableData
        );
    }
}
