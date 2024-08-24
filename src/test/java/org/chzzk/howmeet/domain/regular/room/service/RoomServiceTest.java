package org.chzzk.howmeet.domain.regular.room.service;

import org.chzzk.howmeet.domain.regular.room.exception.RoomException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.ROOM_NOT_FOUND;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMemberRepository roomMemberRepository;

    @Mock
    private MSRepository msRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    @DisplayName("ID로 방 조회 시 잘못된 ID로 예외 발생 테스트")
    void getRoomByIdInvalidTest() throws Exception {
        // when
        Long invalidId = 999L;
        when(roomRepository.findById(invalidId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> roomService.getRoom(invalidId))
                .isInstanceOf(RoomException.class)  // 여기 수정: 예외를 RoomException으로 수정
                .hasMessage(ROOM_NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("방 삭제 시 잘못된 ID로 예외 발생 테스트")
    void deleteRoomInvalidTest() throws Exception {
        // given
        Long invalidId = 999L;

        // when & then
        assertThatThrownBy(() -> roomService.deleteRoom(invalidId))
                .isInstanceOf(RoomException.class)
                .hasMessage(ROOM_NOT_FOUND.getMessage());
    }

}
