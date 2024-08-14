package org.chzzk.howmeet.domain.regular.room.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.room.model.converter.RoomDescriptionConverter;
import org.chzzk.howmeet.domain.regular.room.model.converter.RoomNameConverter;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = RoomNameConverter.class)
    @Column(name = "name", nullable = false)
    private RoomName name;

    @Convert(converter = RoomDescriptionConverter.class)
    @Column(name = "description", nullable = false)
    private RoomDescription description;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberSchedule> schedules;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMember> members;

    public Room(final RoomDescription description, final RoomName name) {
        this.description = description;
        this.name = name;
    }

    public void updateDescription(final RoomDescription description) {
        if (!description.isNullOrEmpty()) {
            this.description = description;
        }
    }

    public void updateName(final RoomName name) {
        if (!name.isNullOrEmpty()) {
            this.name = name;
        }
    }
}
