package org.chzzk.howmeet.domain.regular.confirm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ConfirmSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @Column(name = "name")
    private List<String> participantName;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "times", nullable = false)
    private List<LocalDateTime> times;

    @Column(name = "member_schedule_id", nullable = false)
    private Long memberScheduleId;


    private ConfirmSchedule(final List<LocalDateTime> times, final List<String> participantNames, final Long memberScheduleId) {
        this.times = times;
        this.participantName = participantNames;
        this.memberScheduleId = memberScheduleId;
    }

    public static ConfirmSchedule of(final List<LocalDateTime> time, final List<String> participantNames, final Long memberScheduleId){
        return new ConfirmSchedule(time, participantNames, memberScheduleId);
    }
}
