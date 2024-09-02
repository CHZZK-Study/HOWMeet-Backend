package org.chzzk.howmeet.domain.regular.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refresh_token")
@Getter
@ToString
public class RefreshToken {
    @Id
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "value", nullable = false, unique = true)
    @Indexed
    private String value;

    @Column(name = "expiration", nullable = false)
    @TimeToLive
    private Long expiration;

    private RefreshToken(final Long memberId,
                         final String value,
                         final Long expiration) {
        this.memberId = memberId;
        this.value = value;
        this.expiration = expiration;
    }

    public static RefreshToken of(final AuthPrincipal authPrincipal, final String value, final Long expiration) {
        if (!authPrincipal.isMember()) {
            throw new IllegalArgumentException();
        }

        return new RefreshToken(authPrincipal.id(), value, expiration);
    }
}
