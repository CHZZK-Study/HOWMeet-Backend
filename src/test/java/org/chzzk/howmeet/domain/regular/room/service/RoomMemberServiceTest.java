package org.chzzk.howmeet.domain.regular.room.service;

import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
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

    @Test
    @DisplayName("방 멤버 업데이트 시 잘못된 방 ID로 예외 발생 테스트")
    void updateRoomMembersInvalidRoomIdTest() {
        // given
        Long invalidRoomId = 999L;
        Room room = RoomFixture.createRoomA();
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


