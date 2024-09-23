package org.chzzk.howmeet.domain.regular.confirm.dto;

import java.time.LocalDateTime;
import java.util.List;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;

public record ConfirmScheduleResponse(RoomName roomName, String msName, List<LocalDateTime> time, List<String> participantMembers, Nicknames totalRoomMembers, List<SelectionDetail> allSelectTime) {

    public static ConfirmScheduleResponse of(final ConfirmSchedule confirmSchedule, final Nicknames totalRoomMembers, final List<SelectionDetail> allSelectTime) {
        return new ConfirmScheduleResponse(
                confirmSchedule.getMs().getRoom().getName(),
                confirmSchedule.getMs().getName().getValue(),
                confirmSchedule.getTimes(),
                confirmSchedule.getParticipantName(),
                totalRoomMembers,
                allSelectTime
        );
    }
}
