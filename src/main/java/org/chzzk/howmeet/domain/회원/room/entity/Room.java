package org.chzzk.howmeet.domain.회원.room.entity;

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
import org.chzzk.howmeet.domain.회원.room.model.RoomDescription;
import org.chzzk.howmeet.domain.회원.room.model.RoomName;
import org.chzzk.howmeet.domain.회원.room.model.converter.RoomDescriptionConverter;
import org.chzzk.howmeet.domain.회원.room.model.converter.RoomNameConverter;

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

    public Room(final RoomDescription description, final RoomName name) {
        this.description = description;
        this.name = name;
    }
}
