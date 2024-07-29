package org.chzzk.howmeet.domain.temporary.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.converter.EncodedPasswordConverter;
import org.chzzk.howmeet.domain.common.model.converter.NicknameConverter;
import org.chzzk.howmeet.domain.common.model.converter.PasswordConverter;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Guest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    @Convert(converter = NicknameConverter.class)
    private Nickname nickname;

    @Column(name = "password", nullable = false)
    @Convert(converter = EncodedPasswordConverter.class)
    private EncodedPassword password;

    // todo 7/19 김민우 : 추후 cascade 를 활용하기 위해 연관관계 사용 고려
    @Column(name = "guest_schedule_id", nullable = false)
    private Long guestScheduleId;

    private Guest(final Nickname nickname, final EncodedPassword password, final Long guestScheduleId) {
        this.nickname = nickname;
        this.password = password;
        this.guestScheduleId = guestScheduleId;
    }

    public static Guest of(final Long guestScheduleId, final String nickname, final EncodedPassword password) {
        return new Guest(Nickname.from(nickname), password, guestScheduleId);
    }
}
