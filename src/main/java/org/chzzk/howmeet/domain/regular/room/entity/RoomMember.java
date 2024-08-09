package org.chzzk.howmeet.domain.regular.room.entity;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "is_leader", nullable = false)
    private Boolean isLeader;

    private RoomMember(final Long memberId, final Room room, final Boolean isLeader) {
        this.memberId = memberId;
        this.room = room;
        this.isLeader = isLeader;
    }

    public static RoomMember of(final Long memberId, final Room room, final Boolean isLeader) {
        return new RoomMember(memberId, room, isLeader);
    }
}
