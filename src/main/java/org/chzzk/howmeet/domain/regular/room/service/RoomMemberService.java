package org.chzzk.howmeet.domain.regular.room.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberResponse;
import org.chzzk.howmeet.domain.regular.room.dto.get.response.RoomMemberGetResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.exception.RoomException;
import org.chzzk.howmeet.domain.regular.room.exception.RoomMemberException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.INVALID_ROOM_MEMBER;
import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.ROOM_MEMBER_NOT_FOUND;
import static org.chzzk.howmeet.domain.regular.room.exception.RoomMemberErrorCode.ROOM_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomMemberService {
    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;

    public RoomMemberGetResponse getRoomMember(final AuthPrincipal authPrincipal, final Long roomId) {
        final RoomMember roomMember = findRoomMemberByAuthAndRoomId(authPrincipal, roomId);
        return RoomMemberGetResponse.from(roomMember);
    }

    @Transactional
    public void deleteRoomMember(final Long roomId, final Long roomMemberId) {
        RoomMember roomMember = findRoomMemberById(roomMemberId);
        if (!roomMember.getRoom().getId().equals(roomId)) {
            throw new RoomException(INVALID_ROOM_MEMBER);
        }
        roomMemberRepository.deleteById(roomMemberId);
    }

    @Transactional
    public void updateRoomMembers(final Long roomId, final List<RoomMemberRequest> roomMemberRequests) {
        Room room = findRoomById(roomId);

        List<RoomMember> newRoomMembers = roomMemberRequests.stream()
                .map(request -> RoomMember.of(request.memberId(), room, request.isLeader()))
                .collect(Collectors.toList());
        roomMemberRepository.saveAll(newRoomMembers);

        List<RoomMember> allRoomMembers = roomMemberRepository.findByRoomId(roomId);
    }

    private Room findRoomById(final Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomMemberException(ROOM_NOT_FOUND));
    }

    private RoomMember findRoomMemberById(final Long roomMemberId) {
        return roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new RoomException(ROOM_MEMBER_NOT_FOUND));
    }

    private RoomMember findRoomMemberByAuthAndRoomId(final AuthPrincipal authPrincipal, final Long roomId) {
        return roomMemberRepository.findByRoomIdAndMemberId(roomId, authPrincipal.id())
                .orElseThrow(() -> new RoomMemberException(ROOM_NOT_FOUND));
    }

}
