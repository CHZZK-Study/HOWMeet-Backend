package org.chzzk.howmeet.domain.temporary.schedule.service;

import lombok.RequiredArgsConstructor;
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

    @Transactional
    public GSResponse createGuestSchedule(final GSRequest gsRequest) {
        GuestSchedule guestSchedule = GuestSchedule.of(gsRequest.dates(), gsRequest.time(), gsRequest.name());
        GuestSchedule savedSchedule = gsRepository.save(guestSchedule);
        String inviteLink = "http://localhost:8080/guest-schedule/invite/" + savedSchedule.getId();
        return GSResponse.of(savedSchedule, inviteLink);
    }

    public GSResponse getGuestSchedule(final Long guestScheduleId) {
        GuestSchedule guestSchedule = gsRepository.findById(guestScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
        String inviteLink = "http://localhost:8080/guest-schedule/invite/" + guestSchedule.getId();
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
