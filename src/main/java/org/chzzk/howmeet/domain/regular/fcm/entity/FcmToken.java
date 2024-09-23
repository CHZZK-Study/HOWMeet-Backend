package org.chzzk.howmeet.domain.regular.fcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken {

    @Id
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "value", nullable = false, unique = true)
    private String value;

    private FcmToken(final Long memberId,
            final String value) {
        this.memberId = memberId;
        this.value = value;
    }

    private boolean isNullOrEmpty() {
        return value == null || value.trim().isEmpty();
    }
    public static FcmToken of(final AuthPrincipal authPrincipal, final String value) {
        if (!authPrincipal.isMember()) {
            throw new IllegalArgumentException("회원이 아니므로 fcm토큰을 저장하실 수 없습니다.");
        }

        return new FcmToken(authPrincipal.id(), value);
    }

    public void updateValue(final String value) {
        if (isNullOrEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 토큰 값입니다.");
        }
        this.value = value;
    }
}
