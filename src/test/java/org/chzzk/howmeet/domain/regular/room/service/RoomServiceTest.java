package org.chzzk.howmeet.domain.regular.room.service;

import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.chzzk.howmeet.fixture.RoomFixture;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    Room room = RoomFixture.ROOM_1.create();

    @Test
    @DisplayName("방 생성 테스트")
    void createRoomTest() throws Exception {
        // given
        RoomRequest roomRequest = new RoomRequest(
                room.getName(),
                room.getDescription(),
                room.getSchedules().get(0),
                room.getMembers().get(0).getMemberId()
        );

        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(msRepository.save(any(MemberSchedule.class))).thenReturn(room.getSchedules().get(0));
        when(roomMemberRepository.save(any(RoomMember.class))).thenReturn(room.getMembers().get(0));

        // when
        RoomResponse roomResponse = roomService.createRoom(roomRequest);

        // then
        assertThat(roomResponse.name()).isEqualTo(room.getName().getValue());
        assertThat(roomResponse.description()).isEqualTo(room.getDescription().getValue());
        verify(roomRepository, times(1)).save(any(Room.class));
        verify(msRepository, times(1)).save(any(MemberSchedule.class));
        verify(roomMemberRepository, times(1)).save(any(RoomMember.class));
    }

    @Test
    @DisplayName("ID로 방 조회 테스트")
    void getRoomByIdTest() throws Exception {
        // given
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomMemberRepository.findByRoomId(room.getId())).thenReturn(room.getMembers());

        // when
        RoomResponse roomResponse = roomService.getRoom(room.getId());

        // then
        assertThat(roomResponse.name()).isEqualTo(room.getName().getValue());
        assertThat(roomResponse.description()).isEqualTo(room.getDescription().getValue());
        assertThat(roomResponse.roomMembers().size()).isEqualTo(room.getMembers().size());
        verify(roomRepository, times(1)).findById(room.getId());
        verify(roomMemberRepository, times(1)).findByRoomId(room.getId());
    }

    @Test
    @DisplayName("ID로 방 조회 시 잘못된 ID로 예외 발생 테스트")
    void getRoomByIdInvalidTest() throws Exception {
        // when
        Long invalidId = 999L;
        when(roomRepository.findById(invalidId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> roomService.getRoom(invalidId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid room ID");
    }

    @Test
    @DisplayName("방 삭제 테스트")
    void deleteRoomTest() throws Exception {
        // given
        when(roomRepository.existsById(room.getId())).thenReturn(true);
        doNothing().when(roomRepository).deleteById(room.getId());

        // when
        roomService.deleteRoom(room.getId());

        // then
        verify(roomRepository, times(1)).deleteById(room.getId());
    }

    @Test
    @DisplayName("방 삭제 시 잘못된 ID로 예외 발생 테스트")
    void deleteRoomInvalidTest() throws Exception {
        // given
        Long invalidId = 999L;
        when(roomRepository.existsById(invalidId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> roomService.deleteRoom(invalidId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid room ID");
    }

    @Test
    @DisplayName("방 업데이트 테스트")
    void updateRoomTest() throws Exception {
        // given
        MemberSchedule memberSchedule = mock(MemberSchedule.class);
        room.setSchedules(List.of(memberSchedule));

        RoomRequest updateRequest = new RoomRequest(
                RoomName.from("Updated Room Name"),
                RoomDescription.from("Updated Room Description"),
                room.getSchedules().get(0),
                room.getMembers().get(0).getMemberId()
        );

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // when
        RoomResponse updatedRoomResponse = roomService.updateRoom(room.getId(), updateRequest);

        // then
        assertThat(updatedRoomResponse.name()).isEqualTo("Updated Room Name");
        assertThat(updatedRoomResponse.description()).isEqualTo("Updated Room Description");
        verify(roomRepository, times(1)).save(any(Room.class));
    }

}
