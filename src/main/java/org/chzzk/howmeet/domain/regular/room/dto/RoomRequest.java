package org.chzzk.howmeet.domain.regular.room.dto;

import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;

public record RoomRequest(RoomName name, RoomDescription description) {
}