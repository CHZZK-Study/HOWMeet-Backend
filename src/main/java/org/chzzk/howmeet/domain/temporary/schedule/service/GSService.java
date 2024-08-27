package org.chzzk.howmeet.domain.temporary.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSRequest;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chzzk.howmeet.domain.temporary.schedule.exception.GSErrorCode.SCHEDULE_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GSService {
    private final GSRepository gsRepository;
    //private final InviteUrlProvider inviteUrlProvider;

    @Transactional
    public GSResponse createGuestSchedule(final GSRequest gsRequest) {
        GuestSchedule guestSchedule = GuestSchedule.of(gsRequest.dates(), gsRequest.time(), gsRequest.name());
        GuestSchedule savedSchedule = gsRepository.save(guestSchedule);
        //String inviteLink = inviteUrlProvider.generateInviteUrl("guest-schedule", savedSchedule.getId());
        return GSResponse.of(savedSchedule);
    }

    public GSResponse getGuestSchedule(final Long guestScheduleId) {
        GuestSchedule guestSchedule = gsRepository.findById(guestScheduleId)
                .orElseThrow(() -> new GSException(SCHEDULE_NOT_FOUND));
        // String inviteLink = inviteUrlProvider.generateInviteUrl("guest-schedule", guestScheduleId);
        return GSResponse.of(guestSchedule);
    }

    @Transactional
    public void deleteGuestSchedule(final Long guestScheduleId) {
        if (!gsRepository.existsById(guestScheduleId)) {
            throw new GSException(SCHEDULE_NOT_FOUND);
        }
        gsRepository.deleteById(guestScheduleId);
    }
}
