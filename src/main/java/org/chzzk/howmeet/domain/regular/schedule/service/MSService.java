package org.chzzk.howmeet.domain.regular.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.confirm.repository.ConfirmRepository;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.dto.*;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
import org.chzzk.howmeet.domain.regular.schedule.exception.MSException;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GSCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.ROOM_NOT_FOUND;
import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.SCHEDULE_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MSService {
    private final MSRepository msRepository;
    private final RoomRepository roomRepository;
    private final ConfirmRepository confirmRepository;

    @Transactional
    public MSCreateResponse createMemberSchedule(final Long roomId, final MSRequest msRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new MSException(ROOM_NOT_FOUND));

        MemberSchedule memberSchedule = msRequest.toEntity(room);
        MemberSchedule savedSchedule = msRepository.save(memberSchedule);

        return MSCreateResponse.from(savedSchedule);
    }

    public MSResponse getMemberSchedule(final Long roomId, final Long msId) {
        MemberSchedule memberSchedule = findMemberScheduleByRoomIdAndMsId(roomId, msId);

        if (memberSchedule.getStatus() == ScheduleStatus.COMPLETE) {
            ConfirmSchedule confirmSchedule = findConfirmScheduleByMSId(msId);
            List<LocalDateTime> confirmTimes = confirmSchedule.getTimes();
            List<String> dates = extractDatesFromConfirmTimes(confirmTimes);
            ScheduleTime scheduleTime = extractScheduleTimeFromConfirmTimes(confirmTimes);
            return CompletedMSResponse.of(memberSchedule, dates, scheduleTime);
        } else if (memberSchedule.getStatus() == ScheduleStatus.PROGRESS) {
            return ProgressedMSResponse.from(memberSchedule);
        }

        throw new IllegalStateException("알 수 없는 상태입니다.");
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

    private ConfirmSchedule findConfirmScheduleByMSId(Long memberScheduleId) {
        return confirmRepository.findByMemberScheduleId(memberScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 일정 결과를 찾을 수 없습니다."));
    }

    private List<String> extractDatesFromConfirmTimes(List<LocalDateTime> confirmTimes) {
        String date = confirmTimes.get(0).toLocalDate().toString();
        return List.of(date);
    }

    private ScheduleTime extractScheduleTimeFromConfirmTimes(List<LocalDateTime> confirmTimes) {
        LocalTime startTime = confirmTimes.get(0).toLocalTime();
        LocalTime endTime = confirmTimes.get(1).toLocalTime();
        return ScheduleTime.of(startTime, endTime);
    }
}

