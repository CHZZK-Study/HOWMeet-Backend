package org.chzzk.howmeet.domain.regular.room.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.member.dto.nickname.dto.MemberNicknameDto;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.room.dto.RoomListResponse;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.exception.RoomException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.room.util.RoomListMapper;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MSRepository msRepository;
    private final MemberRepository memberRepository;

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

    public List<RoomListResponse> getJoinedRooms(final Long memberId) {
        List<RoomMember> roomMembers = roomMemberRepository.findByMemberId(memberId);

        return roomMembers.stream()
                .map(this::mapToRoomListResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoomResponse updateRoom(final Long roomId, final RoomRequest roomRequest) {
        Room room = getRoomById(roomId);
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

        return RoomListMapper.toRoomListResponse(room, memberSchedules, leaderNickname);
    }

    private Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ROOM_NOT_FOUND));
    }
}
