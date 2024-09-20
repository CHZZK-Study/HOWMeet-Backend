package org.chzzk.howmeet.domain.regular.room.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.room.dto.*;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.exception.RoomException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.room.util.RoomListMapper;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Long createRoom(final RoomRequest roomRequest) {
        Room room = roomRequest.toEntity();
        roomRepository.save(room);
        RoomMember leader = RoomMember.createLeaderRoomMember(roomRequest.leaderMemberId(), room);
        roomMemberRepository.save(leader);
        return room.getId();
    }

    public RoomResponse getRoom(final Long roomId) {
        Room room = getRoomById(roomId);
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        List<RoomMemberResponse> roomMemberResponses = roomMembers.stream()
                .map(roomMember -> {
                    Nickname nickname = memberRepository.findById(roomMember.getMemberId())
                            .map(Member::getNickname)
                            .orElse(null);
                    return RoomMemberResponse.of(roomMember, nickname.getValue());
                })
                .toList();
        List<MSResponse> schedules = room.getSchedules().stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .map(MSResponse::from)
                .toList();
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

        return RoomListMapper.toRoomListResponse(room, memberSchedules, leaderNickname);
    }

    private Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ROOM_NOT_FOUND));
    }
}
