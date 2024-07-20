package org.chzzk.howmeet.domain.회원.room.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RoomMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "is_leader", nullable = false)
    private Boolean isLeader;

    private RoomMember(final Long memberId, final Long roomId, final Boolean isLeader) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.isLeader = isLeader;
    }
}
