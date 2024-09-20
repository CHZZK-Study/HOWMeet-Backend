package org.chzzk.howmeet.domain.regular.notice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.regular.fcm.entity.FcmToken;
import org.chzzk.howmeet.domain.regular.notice.model.NoticeMessageTemplate;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.notice.model.NoticeStatus;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private FcmToken fcmToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_schedule_id")
    private MemberSchedule ms;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NoticeStatus status;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false)
    private String body;

    private Notice(final FcmToken fcmToken, final MemberSchedule ms, final NoticeStatus status, final NoticeMessageTemplate template){
        this.fcmToken = fcmToken;
        this.ms = ms;
        this.status = status;

        this.title = template.formatTitle(ms.getName().getValue());
        this.body = template.formatBody(ms.getRoom().getName().getValue(), ms.getName().getValue());
    }

    public static Notice of(final FcmToken fcmToken, final MemberSchedule ms, final NoticeStatus status, final NoticeMessageTemplate template){
        return new Notice(fcmToken, ms, status, template);
    }
}
