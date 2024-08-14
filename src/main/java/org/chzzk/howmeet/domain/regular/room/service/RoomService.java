package org.chzzk.howmeet.domain.regular.room.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        RoomMember leader = RoomMember.of(roomRequest.leaderMemberId(), savedRoom, true);
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
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        List<MemberSchedule> memberSchedules = room.getSchedules();
        return RoomResponse.of(room, roomMembers, memberSchedules);
    }

    @Transactional
    public RoomResponse updateRoom(final Long roomId, final RoomRequest roomRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        room.updateDescription(roomRequest.description());
        room.updateName(roomRequest.name());
        roomRepository.save(room);

        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        List<MemberSchedule> memberSchedules = room.getSchedules();
        return RoomResponse.of(room, roomMembers, memberSchedules);
    }

    @Transactional
    public RoomResponse updateRoomMembers(final Long roomId, final List<RoomMemberRequest> roomMemberRequests) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        List<RoomMember> newRoomMembers = roomMemberRequests.stream()
                .map(request -> RoomMember.of(request.memberId(), room, request.isLeader()))
                .collect(Collectors.toList());
        roomMemberRepository.saveAll(newRoomMembers);

        List<RoomMember> allRoomMembers = roomMemberRepository.findByRoomId(roomId);
        List<MemberSchedule> memberSchedules = room.getSchedules();
        return RoomResponse.of(room, allRoomMembers, memberSchedules);
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new RuntimeException("Invalid room ID");
        }
        roomRepository.deleteById(roomId);
    }

    @Transactional
    public void deleteRoomMember(final Long roomId, final Long roomMemberId) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room member ID"));
        if (!roomMember.getRoom().getId().equals(roomId)) {
            throw new IllegalArgumentException("Room member does not belong to the specified room");
        }
        roomMemberRepository.deleteById(roomMemberId);
    }
}
