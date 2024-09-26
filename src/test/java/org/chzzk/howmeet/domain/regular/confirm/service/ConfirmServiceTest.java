package org.chzzk.howmeet.domain.regular.confirm.service;

import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.confirm.dto.ConfirmScheduleRequest;
import org.chzzk.howmeet.domain.regular.confirm.dto.ConfirmScheduleResponse;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.confirm.repository.ConfirmRepository;
import org.chzzk.howmeet.domain.regular.fcm.service.FcmService;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.record.repository.MSRecordRepository;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.chzzk.howmeet.fixture.MSFixture;
import org.chzzk.howmeet.fixture.MemberFixture;
import org.chzzk.howmeet.fixture.RoomFixture;
import org.chzzk.howmeet.fixture.RoomMemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConfirmServiceTest{
    @Mock
    MSRepository msRepository;
    @Mock
    RoomMemberRepository roomMemberRepository;
    @Mock
    MSRecordRepository msRecordRepository;
    @Mock
    ConfirmRepository confirmRepository;
    @Mock
    FcmService fcmService;
    @InjectMocks
    ConfirmService confirmService;

    Member member = MemberFixture.KIM.생성();
    MemberSchedule ms = MSFixture.createMemberScheduleA(RoomFixture.createRoomA());

    AuthPrincipal authPrincipal = new AuthPrincipal(1L, member.getNickname().getValue(), member.getRole());
    Room room = RoomFixture.createRoomA();
    RoomMember leader = RoomMemberFixture.MEMBER_1.create(room);
    List<LocalDateTime> selectTimes = Arrays.asList(
            LocalDateTime.of(2023, 1, 1, 10, 30),
            LocalDateTime.of(2023, 1, 1, 11, 0),
            LocalDateTime.of(2023, 1, 1, 11, 30)
    );
    List<String> names = Arrays.asList("김민우", "김수현", "이채림");
    ConfirmScheduleRequest request = new ConfirmScheduleRequest(selectTimes, names);

    @Test
    @DisplayName("일정 확정 입력 성공 테스트")
    public void postConfirmSchedule() throws Exception {
        ConfirmSchedule confirmSchedule = request.toEntity(1L);

        when(msRepository.findById(ms.getId())).thenReturn(Optional.of(ms));
        when(roomMemberRepository.findByRoomId(anyLong())).thenReturn(List.of(leader));
        when(confirmRepository.save(any(ConfirmSchedule.class))).thenAnswer(invocation -> {
            ConfirmSchedule savedSchedule = invocation.getArgument(0);

            Field idField = savedSchedule.getClass().getDeclaredField("id");

            idField.setAccessible(true);
            idField.set(savedSchedule, 1L);
            return savedSchedule;
        });

        Long confirmScheduleId = confirmService.postConfirmSchedule(1L, request, authPrincipal);

        verify(confirmRepository).save(any(ConfirmSchedule.class));
        verify(fcmService).sendToRoomMember(anyList(), eq(ms));

        // note: confirmSchedule에 따로 id값을 세팅할 방법이 없음.
        assertThat(confirmScheduleId).isNotNull();
        assertThat(confirmScheduleId).isEqualTo(1L);
    }

    @Test
    @DisplayName("일정 확정 조회 성공 테스트")
    public void getConfirmScheduleSuccess() {
        ConfirmSchedule confirmSchedule = request.toEntity(1L);

        when(confirmRepository.findByMemberScheduleId(1L)).thenReturn(Optional.of(confirmSchedule));
        when(msRecordRepository.findByMemberScheduleId(1L)).thenReturn(Collections.emptyList());
        when(msRepository.findById(1L)).thenReturn(Optional.of(ms));
        ConfirmScheduleResponse response = confirmService.getConfirmSchedule(1L, 1L);

        assertThat(response).isNotNull();
        verify(confirmRepository).findByMemberScheduleId(1L);
        verify(msRecordRepository).findByMemberScheduleId(1L);
    }

    @Test
    @DisplayName("일정 확정 조회 실패 테스트")
    public void getConfirmSchedule_NotFound() {
        when(confirmRepository.findByMemberScheduleId(1999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            confirmService.getConfirmSchedule(1L, 1999L);
        });


        verify(confirmRepository).findByMemberScheduleId(1999L);
    }
}