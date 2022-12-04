package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.LectureRoom;
import ua.foxminded.university.dto.room.RoomResponseDTO;
import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.service.interfaces.LectureRoomService;

import java.util.List;

@Component
public class RoomMapper {

    private final LectureRoomService roomService;

    @Autowired
    public RoomMapper(LectureRoomService roomService) {
        this.roomService = roomService;
    }

    public LectureRoom convertToRoom(RoomRequestDTO roomDTO) {
        return new ModelMapper().map(roomDTO, LectureRoom.class);
    }

    public RoomResponseDTO convertToRoomResponseDTO(LectureRoom room) {
        return new ModelMapper().map(room, RoomResponseDTO.class);
    }

    public RoomRequestDTO convertToRoomRequestDTO(LectureRoom room) {
        return new ModelMapper().map(room, RoomRequestDTO.class);
    }

    public List<RoomRequestDTO> getRoomsDTO() {
        return roomService.findAll().stream()
            .map(this::convertToRoomRequestDTO)
            .toList();
    }
}
