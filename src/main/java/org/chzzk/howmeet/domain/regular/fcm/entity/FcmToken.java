package org.chzzk.howmeet.domain.regular.fcm.entity;

import static org.chzzk.howmeet.domain.regular.fcm.exception.FcmErrorCode.FCMTOKEN_INVALID_VALUE;
import static org.chzzk.howmeet.domain.regular.fcm.exception.FcmErrorCode.MEMBER_UNAUTHORIZED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.fcm.exception.FcmErrorException;

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
            throw new FcmErrorException(MEMBER_UNAUTHORIZED);
        }

        return new FcmToken(authPrincipal.id(), value);
    }

    public void updateValue(final String value) {
        if (isNullOrEmpty()) {
            throw new FcmErrorException(FCMTOKEN_INVALID_VALUE);
        }
        this.value = value;
    }
}
