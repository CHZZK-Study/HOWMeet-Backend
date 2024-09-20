package org.chzzk.howmeet.domain.regular.confirm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

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

    @ElementCollection
    @Column(name = "time", nullable = false)
    private List<LocalDateTime> time;

    @OneToOne
    @JoinColumn(name = "member_schedule_id", nullable = false)
    private MemberSchedule ms;
    private ConfirmSchedule(final List<LocalDateTime> time, final List<String> participantNames, final MemberSchedule ms) {
        this.time = time;
        this.participantName = participantNames;
        this.ms = ms;
    }

    public static ConfirmSchedule of(final List<LocalDateTime> time, final List<String> participantNames, final MemberSchedule ms){
        return new ConfirmSchedule(time, participantNames, ms);
    }
}
