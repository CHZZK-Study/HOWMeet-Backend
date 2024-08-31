package org.chzzk.howmeet.domain.temporary.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSRequest;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.chzzk.howmeet.domain.temporary.schedule.exception.GSErrorCode.SCHEDULE_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GSService {
    private final GSRepository gsRepository;

    @Transactional
    public GSResponse createGuestSchedule(final GSRequest gsRequest) {
        GuestSchedule guestSchedule = GuestSchedule.of(gsRequest.dates(), gsRequest.time(), gsRequest.name());
        GuestSchedule savedSchedule = gsRepository.save(guestSchedule);
        return GSResponse.of(savedSchedule);
    }

    public GSResponse getGuestSchedule(final Long guestScheduleId) {
        GuestSchedule guestSchedule = gsRepository.findById(guestScheduleId)
                .orElseThrow(() -> new GSException(SCHEDULE_NOT_FOUND));
        return GSResponse.of(guestSchedule);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void disableOldGuestSchedules() {
        LocalDateTime now = LocalDateTime.now();
        // PROGRESS 상태인 스케줄 삭제
        List<GuestSchedule> progressSchedules = gsRepository.findByStatusAndCreatedAtBefore(ScheduleStatus.PROGRESS, now.minusDays(10));
        for (GuestSchedule schedule : progressSchedules) {
            schedule.disable();
        }

        // COMPLETE 상태인 스케줄 삭제
        List<GuestSchedule> completeSchedules = gsRepository.findByStatusAndUpdatedAtBefore(ScheduleStatus.COMPLETE, now.minusDays(10));
        for (GuestSchedule schedule : completeSchedules) {
            schedule.disable();
        }
    }
}
