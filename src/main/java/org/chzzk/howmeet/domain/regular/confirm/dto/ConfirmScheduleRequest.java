package org.chzzk.howmeet.domain.regular.confirm.dto;


import java.time.LocalDateTime;
import java.util.List;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

public record ConfirmScheduleRequest(List<LocalDateTime> time, List<String> participantPerson) {
    public ConfirmSchedule toEntity(final MemberSchedule ms) {
        return ConfirmSchedule.of(time, participantPerson, ms);
    }

}
