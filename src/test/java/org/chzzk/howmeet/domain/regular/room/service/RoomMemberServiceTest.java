package org.chzzk.howmeet.domain.regular.room.service;

import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.exception.RoomMemberException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.fixture.RoomFixture;
import org.chzzk.howmeet.fixture.RoomMemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.regular.room.exception.RoomMemberErrorCode.ROOM_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomMemberServiceTest {

    @Mock
    private RoomMemberRepository roomMemberRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomMemberService roomMemberService;

    Room room = RoomFixture.ROOM_1.create();

    @Test
    @DisplayName("방 멤버 업데이트 테스트")
    void updateRoomMembersTest() {
        // given
        RoomMemberRequest memberRequest1 = new RoomMemberRequest(RoomMemberFixture.MEMBER_1.create(room).getMemberId(), room.getId(), true);
        RoomMemberRequest memberRequest2 = new RoomMemberRequest(RoomMemberFixture.MEMBER_2.create(room).getMemberId(), room.getId(), false);
        List<RoomMemberRequest> roomMemberRequests = List.of(memberRequest1, memberRequest2);

        RoomMember roomMember1 = RoomMemberFixture.MEMBER_1.create(room);
        RoomMember roomMember2 = RoomMemberFixture.MEMBER_2.create(room);
        List<RoomMember> newRoomMembers = List.of(roomMember1, roomMember2);

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomMemberRepository.saveAll(any(List.class))).thenReturn(newRoomMembers);
        when(roomMemberRepository.findByRoomId(room.getId())).thenReturn(newRoomMembers);

        // when
        List<RoomMemberResponse> roomMemberResponses = roomMemberService.updateRoomMembers(room.getId(), roomMemberRequests);

        // then
        assertThat(roomMemberResponses.size()).isEqualTo(2);
        assertThat(roomMemberResponses.get(0).memberId()).isEqualTo(RoomMemberFixture.MEMBER_1.create(room).getMemberId());
        assertThat(roomMemberResponses.get(0).isLeader()).isTrue();
        assertThat(roomMemberResponses.get(1).memberId()).isEqualTo(RoomMemberFixture.MEMBER_2.create(room).getMemberId());
        assertThat(roomMemberResponses.get(1).isLeader()).isFalse();

        verify(roomRepository, times(1)).findById(room.getId());
        verify(roomMemberRepository, times(1)).saveAll(any(List.class));
        verify(roomMemberRepository, times(1)).findByRoomId(room.getId());
    }

    @Test
    @DisplayName("방 멤버 업데이트 시 잘못된 방 ID로 예외 발생 테스트")
    void updateRoomMembersInvalidRoomIdTest() {
        // given
        Long invalidRoomId = 999L;
        RoomMemberRequest memberRequest1 = new RoomMemberRequest(RoomMemberFixture.MEMBER_1.create(room).getMemberId(), invalidRoomId, true);
        List<RoomMemberRequest> roomMemberRequests = List.of(memberRequest1);

        when(roomRepository.findById(invalidRoomId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> roomMemberService.updateRoomMembers(invalidRoomId, roomMemberRequests))
                .isInstanceOf(RoomMemberException.class)  // 예외를 RoomMemberException으로 설정
                .hasMessage(ROOM_NOT_FOUND.getMessage());  // RoomMemberErrorCode.ROOM_NOT_FOUND의 메시지와 일치시켜야 합니다.

        verify(roomRepository, times(1)).findById(invalidRoomId);
        verify(roomMemberRepository, never()).saveAll(any(List.class));
    }
}


