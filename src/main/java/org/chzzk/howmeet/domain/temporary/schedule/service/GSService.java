package org.chzzk.howmeet.domain.temporary.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.util.InviteUrlProvider;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSRequest;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSResponse;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GSService {
    private final GSRepository gsRepository;
    private final InviteUrlProvider inviteUrlProvider;

    @Transactional
    public GSResponse createGuestSchedule(final GSRequest gsRequest) {
        GuestSchedule guestSchedule = GuestSchedule.of(gsRequest.dates(), gsRequest.time(), gsRequest.name());
        GuestSchedule savedSchedule = gsRepository.save(guestSchedule);
        String inviteLink = inviteUrlProvider.generateInviteUrl("guest-schedule", savedSchedule.getId());
        return GSResponse.of(savedSchedule, inviteLink);
    }

    public GSResponse getGuestSchedule(final Long guestScheduleId) {
        GuestSchedule guestSchedule = gsRepository.findById(guestScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
        String inviteLink = inviteUrlProvider.generateInviteUrl("guest-schedule", guestScheduleId);
        return GSResponse.of(guestSchedule, inviteLink);
    }

    @Transactional
    public void deleteGuestSchedule(final Long guestScheduleId) {
        if (!gsRepository.existsById(guestScheduleId)) {
            throw new RuntimeException("Invalid schedule ID");
        }
        gsRepository.deleteById(guestScheduleId);
    }
}
