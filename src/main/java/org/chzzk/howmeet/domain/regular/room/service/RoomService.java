package org.chzzk.howmeet.domain.regular.room.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
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
    public RoomResponse createRoom(RoomRequest roomRequest) {
        Room room = new Room(roomRequest.description(), roomRequest.name());
        Room savedRoom = roomRepository.save(room);
        RoomMember leader = RoomMember.of(roomRequest.leaderMemberId(), savedRoom, true);
        roomMemberRepository.save(leader);

        MemberSchedule memberSchedule = roomRequest.memberSchedule();
        memberSchedule.setRoom(savedRoom);
        msRepository.save(memberSchedule);

        return RoomResponse.of(savedRoom, List.of(leader));
    }

    public RoomResponse getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        return RoomResponse.of(room, roomMembers);
    }

    @Transactional
    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        room.updateDescription(roomRequest.description());
        room.updateName(roomRequest.name());
        roomRepository.save(room);

        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        return RoomResponse.of(room, roomMembers);
    }

    @Transactional
    public RoomResponse updateRoomMembers(Long roomId, List<RoomMemberRequest> roomMemberRequests) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        List<RoomMember> newRoomMembers = roomMemberRequests.stream()
                .map(request -> RoomMember.of(request.memberId(), room, request.isLeader()))
                .collect(Collectors.toList());
        roomMemberRepository.saveAll(newRoomMembers);

        List<RoomMember> allRoomMembers = roomMemberRepository.findByRoomId(roomId);
        return RoomResponse.of(room, allRoomMembers);
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new RuntimeException("Invalid room ID");
        }
        roomRepository.deleteById(roomId);
    }

    @Transactional
    public void deleteRoomMember(Long roomId, Long roomMemberId) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room member ID"));
        if (!roomMember.getRoom().getId().equals(roomId)) {
            throw new IllegalArgumentException("Room member does not belong to the specified room");
        }
        roomMemberRepository.deleteById(roomMemberId);
    }
}
