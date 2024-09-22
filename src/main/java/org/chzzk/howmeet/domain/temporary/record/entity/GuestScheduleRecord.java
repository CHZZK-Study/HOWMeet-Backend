package org.chzzk.howmeet.domain.temporary.record.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
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

    @Column(name = "select_time")
    private LocalDateTime selectTime;

    @Column(name = "guest_id", nullable = false)
    private Long guestId;

    @Column(name = "guest_schedule_id", nullable = false)
    private Long guestScheduleId;

    private GuestScheduleRecord(final Long guestId, final Long guestScheduleId, final LocalDateTime selectTime) {
        this.guestId = guestId;
        this.guestScheduleId = guestScheduleId;
        this.selectTime = selectTime;
    }

    public static GuestScheduleRecord of(final Guest guest, final GuestSchedule guestSchedule, final LocalDateTime selectTime) {
        return new GuestScheduleRecord(guest.getId(), guestSchedule.getId(), selectTime);
    }
}
