package org.chzzk.howmeet.domain.regular.room.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.exception.RoomException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MSRepository msRepository;

    @Transactional
    public RoomResponse createRoom(final RoomRequest roomRequest) {
        Room room = roomRequest.toEntity();
        Room savedRoom = roomRepository.save(room);
        RoomMember leader = RoomMember.createLeaderRoomMember(roomRequest.leaderMemberId(), savedRoom);
        roomMemberRepository.save(leader);

        MSRequest msRequest = roomRequest.msRequest();
        MemberSchedule memberSchedule = MemberSchedule.of(
                msRequest.dates(),
                msRequest.time(),
                msRequest.name(),
                savedRoom
        );

        msRepository.save(memberSchedule);
        return RoomResponse.of(savedRoom, List.of(leader), List.of(memberSchedule));
    }

    public RoomResponse getRoom(final Long roomId) {
        Room room = getRoomById(roomId);
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        List<MemberSchedule> memberSchedules = room.getSchedules();
        return RoomResponse.of(room, roomMembers, memberSchedules);
    }

    @Transactional
    public RoomResponse updateRoom(final Long roomId, final RoomRequest roomRequest) {
        Room room = getRoomById(roomId);
        room.updateDescription(roomRequest.description());
        room.updateName(roomRequest.name());
        roomRepository.save(room);

        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        List<MemberSchedule> memberSchedules = room.getSchedules();
        return RoomResponse.of(room, roomMembers, memberSchedules);
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = getRoomById(roomId);
        roomRepository.delete(room);
    }

    @Transactional
    public void deleteRoomMember(final Long roomId, final Long roomMemberId) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new RoomException(ROOM_MEMBER_NOT_FOUND));
        if (!roomMember.getRoom().getId().equals(roomId)) {
            throw new RoomException(INVALID_ROOM_MEMBER);
        }
        roomMemberRepository.deleteById(roomMemberId);
    }

    private Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ROOM_NOT_FOUND));
    }
}
