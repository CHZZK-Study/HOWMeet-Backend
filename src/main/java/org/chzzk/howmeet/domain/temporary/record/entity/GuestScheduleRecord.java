package org.chzzk.howmeet.domain.temporary.record.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.common.embedded.date.impl.RecordDate;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GuestScheduleRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private RecordDate date;

    @Column(name = "guest_id", nullable = false)
    private Long guestId;

    @Column(name = "guest_schedule_id", nullable = false)
    private Long guestScheduleId;

    private GuestScheduleRecord(final RecordDate date, final Long guestId, final Long guestScheduleId) {
        this.date = date;
        this.guestId = guestId;
        this.guestScheduleId = guestScheduleId;
    }

    public static GuestScheduleRecord of(final Guest guest, final GuestSchedule guestSchedule, final RecordDate date) {
        return new GuestScheduleRecord(date, guestSchedule.getId(), guest.getId());
    }
}
