package org.chzzk.howmeet.domain.regular.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MSService {
    private final MSRepository msRepository;

    @Transactional
    public MSResponse createMemberSchedule(MSRequest memberScheduleRequest) {
        MemberSchedule memberSchedule = MemberSchedule.of(memberScheduleRequest.date(), memberScheduleRequest.name(), memberScheduleRequest.room());
        MemberSchedule savedSchedule = msRepository.save(memberSchedule);
        String inviteLink = "http://localhost:8080/member-schedule/invite/" + savedSchedule.getId();
        return MSResponse.of(savedSchedule, inviteLink);
    }

    public MSResponse getMemberSchedule(Long memberScheduleId) {
        MemberSchedule memberSchedule = msRepository.findById(memberScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
        String inviteLink = "http://localhost:8080/member-schedule/invite/" + memberSchedule.getId();
        return MSResponse.of(memberSchedule, inviteLink);
    }

    @Transactional
    public void deleteMemberSchedule(Long memberScheduleId) {
        msRepository.deleteById(memberScheduleId);
    }
}
