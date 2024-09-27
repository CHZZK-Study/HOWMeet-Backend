package org.chzzk.howmeet.domain.regular.room.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.confirm.repository.ConfirmRepository;
import org.chzzk.howmeet.domain.regular.member.dto.nickname.dto.MemberNicknameDto;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.record.repository.MSRecordRepository;
import org.chzzk.howmeet.domain.regular.room.dto.*;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.exception.RoomException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.room.util.RoomListMapper;
import org.chzzk.howmeet.domain.regular.schedule.dto.CompletedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.ProgressedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.ROOM_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRepository memberRepository;
    private final ConfirmRepository confirmRepository;
    private final MSRecordRepository msRecordRepository;

    @Transactional
    public RoomCreateResponse createRoom(final RoomRequest roomRequest) {
        Room room = roomRequest.toEntity();
        roomRepository.save(room);
        RoomMember leader = RoomMember.createLeaderRoomMember(roomRequest.leaderMemberId(), room);
        roomMemberRepository.save(leader);
        return RoomCreateResponse.from(room);
    }

    public RoomResponse getRoom(final Long roomId) {
        Room room = roomRepository.findRoomWithMembersAndNicknames(roomId)
                .orElseThrow(() -> new RoomException(ROOM_NOT_FOUND));

        List<Long> memberIds = room.getMembers().stream()
                .map(RoomMember::getMemberId)
                .toList();

        List<MemberNicknameDto> memberNicknames = memberRepository.findNicknamesByMemberIds(memberIds);

        Map<Long, String> memberIdToNicknameMap = memberNicknames.stream()
                .collect(Collectors.toMap(
                        MemberNicknameDto::id,
                        dto -> dto.nickname().getValue()
                ));

        List<RoomMemberResponse> roomMemberResponses = room.getMembers().stream()
                .map(roomMember -> RoomMemberResponse.of(roomMember, memberIdToNicknameMap.get(roomMember.getMemberId())))
                .collect(Collectors.toList());

        List<MSResponse> schedules = room.getSchedules().stream()
                .sorted(Comparator.comparing(BaseEntity::getCreatedAt))
                .map(memberSchedule -> {
                    if (memberSchedule.getStatus() == ScheduleStatus.COMPLETE) {
                        ConfirmSchedule confirmSchedule = findConfirmScheduleByMSId(memberSchedule.getId());
                        List<LocalDateTime> confirmTimes = confirmSchedule.getTimes();
                        List<String> dates = extractDatesFromConfirmTimes(confirmTimes);
                        ScheduleTime scheduleTime = extractScheduleTimeFromConfirmTimes(confirmTimes);
                        return CompletedMSResponse.of(memberSchedule, dates, scheduleTime);
                    } else {
                        return ProgressedMSResponse.from(memberSchedule);
                    }
                })
                .collect(Collectors.toList());

        return RoomResponse.of(room, roomMemberResponses, schedules);
    }

    public PageResponse getJoinedRooms(final Long memberId, final Pageable pageable) {
        Page<RoomMember> roomMembersPage = roomMemberRepository.findByMemberId(memberId, pageable);

        List<RoomListResponse> roomListResponses = roomMembersPage.getContent().stream()
                .map(this::mapToRoomListResponse)
                .collect(Collectors.toList());

        return PageResponse.of(
                roomListResponses,
                pageable.getPageNumber(),
                roomMembersPage.getTotalPages(),
                roomMembersPage.hasNext()
        );
    }

    @Transactional
    public void updateRoom(final Long roomId, final RoomRequest roomRequest) {
        Room room = getRoomById(roomId);
        room.updateName(roomRequest.name());
        roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(final Long roomId) {
        Room room = getRoomById(roomId);
        roomRepository.delete(room);
    }

    private RoomListResponse mapToRoomListResponse(RoomMember roomMember) {
        Room room = roomMember.getRoom();
        List<MemberSchedule> memberSchedules = room.getSchedules();

        String leaderNickname = room.getMembers().stream()
                .filter(RoomMember::getIsLeader)
                .findFirst()
                .map(leader -> memberRepository.findIdAndNicknameById(leader.getMemberId())
                        .map(memberNicknameDto -> memberNicknameDto.nickname().getValue())
                        .orElse(null))
                .orElse(null);

        return RoomListMapper.toRoomListResponse(room, memberSchedules, leaderNickname, this::findConfirmScheduleByMSId, msId -> msRecordRepository.existsByMemberScheduleIdAndMemberId(msId, roomMember.getMemberId()));
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

    private Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ROOM_NOT_FOUND));
    }
}
