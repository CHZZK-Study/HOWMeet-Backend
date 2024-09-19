package org.chzzk.howmeet.domain.regular.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.exception.MSException;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.ROOM_NOT_FOUND;
import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.SCHEDULE_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MSService {
    private final MSRepository msRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public MSResponse createMemberSchedule(final Long roomId, final MSRequest msRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new MSException(ROOM_NOT_FOUND));

        MemberSchedule memberSchedule = msRequest.toEntity(room);
        MemberSchedule savedSchedule = msRepository.save(memberSchedule);

        return MSResponse.from(savedSchedule);
    }

    public MSResponse getMemberSchedule(final Long roomId, final Long msId) {
        MemberSchedule memberSchedule = findMemberScheduleByRoomIdAndMsId(roomId, msId);
        return MSResponse.from(memberSchedule);
    }

    @Transactional
    public void deleteMemberSchedule(final Long roomId, final Long msId) {
        MemberSchedule memberSchedule = findMemberScheduleByRoomIdAndMsId(roomId, msId);
        memberSchedule.deactivate();
        msRepository.save(memberSchedule);
    }

    private MemberSchedule findMemberScheduleByRoomIdAndMsId(Long roomId, Long msId) {
        return msRepository.findByIdAndRoomId(msId, roomId)
                .orElseThrow(() -> new MSException(SCHEDULE_NOT_FOUND));
    }
}

