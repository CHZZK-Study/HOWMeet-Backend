package org.chzzk.howmeet.domain.regular.schedule.service;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.chzzk.howmeet.fixture.MSFixture;
import org.chzzk.howmeet.fixture.RoomFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MSServiceTest {
    @Mock
    MSRepository msRepository;

    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    MSService msService;
    
    Room room = RoomFixture.ROOM_1.create();
    MemberSchedule memberSchedule = MSFixture.MEETING_A.create(room);
    MSRequest msRequest = new MSRequest(memberSchedule.getDates(), memberSchedule.getTime(), memberSchedule.getName(), room.getId());
    MSResponse msResponse = MSResponse.of(memberSchedule, "http://localhost:8080/member-schedule/invite/" + memberSchedule.getId());

    @Test
    @DisplayName("멤버 일정 생성")
    public void createMemberSchedule() throws Exception {
        // given
        final MSResponse expected = msResponse;

        doReturn(Optional.of(room)).when(roomRepository).findById(room.getId());

        // when
        doReturn(memberSchedule).when(msRepository).save(any(MemberSchedule.class));
        final MSResponse actual = msService.createMemberSchedule(msRequest);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("멤버 일정 조회")
    public void getMemberSchedule() throws Exception {
        // given
        final MSResponse expected = msResponse;

        // when
        doReturn(Optional.of(memberSchedule)).when(msRepository).findById(memberSchedule.getId());
        final MSResponse actual = msService.getMemberSchedule(memberSchedule.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("멤버 일정 조회 시 잘못된 ID로 예외 발생")
    public void getMemberScheduleWhenInvalidId() throws Exception {
        // given
        Long invalidId = 999L;

        // when
        doReturn(Optional.empty()).when(msRepository).findById(invalidId);

        // then
        assertThatThrownBy(() -> msService.getMemberSchedule(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid schedule ID");
    }

    @Test
    @DisplayName("멤버 일정 삭제")
    public void deleteMemberSchedule() throws Exception {
        // given
        Long validId = memberSchedule.getId();

        doReturn(true).when(msRepository).existsById(validId);

        // when
        doNothing().when(msRepository).deleteById(validId);
        msService.deleteMemberSchedule(validId);

        // then
        verify(msRepository, times(1)).deleteById(validId);
    }

    @Test
    @DisplayName("멤버 일정 삭제 시 잘못된 ID로 예외 발생")
    public void deleteMemberScheduleWhenInvalidId() throws Exception {
        // given
        Long invalidId = 999L;

        // when
        doReturn(false).when(msRepository).existsById(invalidId);

        // then
        assertThatThrownBy(() -> msService.deleteMemberSchedule(invalidId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid schedule ID");
    }
}
