package org.chzzk.howmeet.domain.regular.room;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;

    @Transactional
    public RoomResponse createRoom(RoomRequest roomRequest) {
        Room room = new Room(roomRequest.description(), roomRequest.name());
        Room savedRoom = roomRepository.save(room);
        return RoomResponse.of(savedRoom);
    }

    public RoomResponse getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        return RoomResponse.of(room);
    }

    @Transactional
    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        room.updateDescription(roomRequest.description());
        room.updateName(roomRequest.name());
        Room updatedRoom = roomRepository.save(room);
        return RoomResponse.of(updatedRoom);
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
