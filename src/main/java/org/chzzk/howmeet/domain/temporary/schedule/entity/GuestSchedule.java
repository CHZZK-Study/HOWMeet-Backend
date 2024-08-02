package org.chzzk.howmeet.domain.temporary.schedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.embedded.date.impl.ScheduleDate;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.common.model.ScheduleName;
import org.chzzk.howmeet.domain.common.model.converter.ScheduleNameConverter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GuestSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ScheduleDate date;

    @Convert(converter = ScheduleNameConverter.class)
    @Column(name = "name", nullable = false)
    private ScheduleName name;

    private GuestSchedule(final ScheduleDate date, final ScheduleName name) {
        this.date = date;
        this.name = name;
    }

    // todo : 생성자랑 시그니처가 동일한데 굳이 정적 팩토리 메서드를 사용하는 이유?
    public static GuestSchedule of(final ScheduleDate date, final ScheduleName name) {
        return new GuestSchedule(date, name);
    }
}