package org.chzzk.howmeet.domain.regular.confirm.dto;


import java.time.LocalDateTime;
import java.util.List;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;

public record ConfirmScheduleRequest(List<LocalDateTime> time, List<String> participantPerson) {
    public ConfirmSchedule toEntity(final Long msId) {
        return ConfirmSchedule.of(time, participantPerson, msId);
    }

}
