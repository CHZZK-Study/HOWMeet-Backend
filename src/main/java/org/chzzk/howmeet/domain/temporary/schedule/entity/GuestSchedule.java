package org.chzzk.howmeet.domain.temporary.schedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleTime;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.common.model.converter.ScheduleNameConverter;
import org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus;

import java.util.List;

import static org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus.COMPLETE;
import static org.chzzk.howmeet.domain.regular.schedule.entity.ScheduleStatus.PROGRESS;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GuestSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @Column(name = "date", nullable = false)
    private List<String> dates;

    @Embedded
    private ScheduleTime time;

    @Convert(converter = ScheduleNameConverter.class)
    @Column(name = "name", nullable = false)
    private ScheduleName name;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    private GuestSchedule(final List<String> dates, final ScheduleTime time, final ScheduleName name) {
        this.dates = dates;
        this.time = time;
        this.name = name;
        this.status = PROGRESS;
    }

    public void complete() {
        this.status = COMPLETE;
    }

    // todo : 생성자랑 시그니처가 동일한데 굳이 정적 팩토리 메서드를 사용하는 이유?
    public static GuestSchedule of(final List<String> dates, final ScheduleTime time, final ScheduleName name) {
        return new GuestSchedule(dates, time, name);
    }
}