package org.chzzk.howmeet.domain.regular.confirm.dto;

import java.time.LocalDateTime;
import java.util.List;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

public record ConfirmScheduleResponse(RoomName roomName, String msName, List<LocalDateTime> confirmTime, List<String> participantPerson, Nicknames totalPersonnel, List<SelectionDetail> time) {

    public static ConfirmScheduleResponse of(final ConfirmSchedule confirmSchedule, final MemberSchedule memberSchedule, final Nicknames totalPersonnel, final List<SelectionDetail> time) {
        return new ConfirmScheduleResponse(
                memberSchedule.getRoom().getName(),
                memberSchedule.getName().getValue(),
                confirmSchedule.getTimes(),
                confirmSchedule.getParticipantName(),
                totalPersonnel,
                time
        );
    }
}
