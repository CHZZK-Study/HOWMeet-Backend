package org.chzzk.howmeet.domain.회원.schedule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleDate;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.common.model.converter.ScheduleNameConverter;
import org.chzzk.howmeet.domain.회원.room.entity.Room;
import org.chzzk.howmeet.domain.회원.room.entity.RoomMember;

import static org.chzzk.howmeet.domain.회원.schedule.entity.ScheduleStatus.COMPLETE;
import static org.chzzk.howmeet.domain.회원.schedule.entity.ScheduleStatus.PROGRESS;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ScheduleDate date;

    @Convert(converter = ScheduleNameConverter.class)
    @Column(name = "name", nullable = false)
    private ScheduleName name;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    private MemberSchedule(final ScheduleDate date, final ScheduleName name, final Long roomId) {
        this.date = date;
        this.name = name;
        this.status = PROGRESS;
        this.roomId = roomId;
    }

    public static MemberSchedule of(final ScheduleDate date, final ScheduleName name, final Room room) {
        return new MemberSchedule(date, name, room.getId());
    }

    public static MemberSchedule of(final ScheduleDate date, final ScheduleName name, final RoomMember roomMember) {
        return new MemberSchedule(date, name, roomMember.getRoomId());
    }

    public void complete() {
        this.status = COMPLETE;
    }
}
