package org.chzzk.howmeet.domain.regular.schedule.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.common.model.converter.ScheduleNameConverter;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.temporary.schedule.util.ListToJsonConverter;

import java.util.List;

import static org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus.COMPLETE;
import static org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus.PROGRESS;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dates", columnDefinition = "json")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> dates;

    @Embedded
    private ScheduleTime time;

    @Convert(converter = ScheduleNameConverter.class)
    @Column(name = "name", nullable = false)
    private ScheduleName name;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    private Room room;

    private MemberSchedule(final List<String> dates, final ScheduleTime time, final ScheduleName name, final Room room) {
        this.dates = dates;
        this.time = time;
        this.name = name;
        this.status = PROGRESS;
        this.room = room;
    }

    public static MemberSchedule of(final List<String> dates, final ScheduleTime time, final ScheduleName name, final Room room) {
        return new MemberSchedule(dates, time, name, room);
    }

    public void complete() {
        this.status = COMPLETE;
    }

    public void updateRoom(final Room room) {
        if (room != null) {
            this.room = room;
        }
    }
}
