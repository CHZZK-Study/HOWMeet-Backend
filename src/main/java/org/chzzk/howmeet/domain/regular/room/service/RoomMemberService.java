package org.chzzk.howmeet.domain.regular.room.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomMemberService {
    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public List<RoomMemberResponse> updateRoomMembers(final Long roomId, final List<RoomMemberRequest> roomMemberRequests) {
        Room room = findRoomById(roomId);

        List<RoomMember> newRoomMembers = roomMemberRequests.stream()
                .map(request -> RoomMember.of(request.memberId(), room, request.isLeader()))
                .collect(Collectors.toList());
        roomMemberRepository.saveAll(newRoomMembers);

        List<RoomMember> allRoomMembers = roomMemberRepository.findByRoomId(roomId);

        return allRoomMembers.stream()
                .map(RoomMemberResponse::from)
                .collect(Collectors.toList());
    }

    private Room findRoomById(final Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
    }
}
