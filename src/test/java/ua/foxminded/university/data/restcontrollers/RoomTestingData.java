package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.dto.room.RoomResponseDTO;

public class RoomTestingData {

    public static final String SHOW_ALL_URL = "/api/rooms";
    public static final String SHOW_URL = "/api/rooms/{id}";
    public static final String EDIT_URL = "/api/rooms/{id}/edit";
    public static final String DELETE_URL = "/api/rooms/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "LectureRoom not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";

    public static final int LIST_SIZE = 4;
    public static final int ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 2;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"number\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public final static String EXISTS_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"Global error\"," +
        "\"rejectedValue\":\"roomRequestDTO\",\"messageError\":\"LectureRoom is already exist\"}]}";

    public RoomRequestDTO generateRoomForCreate() {
        return RoomRequestDTO.builder()
            .id(0)
            .number(105)
            .capacity(50)
            .build();
    }

    public RoomRequestDTO generateRoomForUpdate() {
        return RoomRequestDTO.builder()
            .id(1)
            .number(101)
            .capacity(10)
            .build();
    }

    public RoomRequestDTO generateUpdatedRoom() {
        return RoomRequestDTO.builder()
            .id(0)
            .number(101)
            .capacity(10)
            .build();
    }

    public RoomResponseDTO generateRoomForFindById() {
        return RoomResponseDTO.builder()
            .id(1)
            .number(101)
            .capacity(10)
            .build();
    }

    public RoomRequestDTO generateInvalidRoom() {
        return RoomRequestDTO.builder()
            .id(0)
            .number(null)
            .capacity(10)
            .build();
    }

    public RoomRequestDTO generateExistedRoom() {
        return RoomRequestDTO.builder()
            .id(0)
            .number(101)
            .capacity(10)
            .build();
    }
}
