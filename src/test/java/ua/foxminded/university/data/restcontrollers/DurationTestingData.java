package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.dto.duration.*;

public class DurationTestingData {

    public static final String SHOW_ALL_URL = "/api/durations";
    public static final String SHOW_URL = "/api/durations/{id}";
    public static final String EDIT_URL = "/api/durations/{id}/edit";
    public static final String DELETE_URL = "/api/durations/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "Duration not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";

    public static final int LIST_SIZE = 6;
    public static final int ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 1;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"startTime\"," +
        "\"rejectedValue\":\"\",\"messageError\":\"Should not be empty\"}]}";

    public final static String EXISTS_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"Global error\"," +
        "\"rejectedValue\":\"durationRequestDTO\",\"messageError\":\"Duration is already exist\"}]}";

    public DurationRequestDTO generateDurationForCreate() {
        return DurationRequestDTO.builder()
            .id(0)
            .startTime("19:10")
            .endTime("21:30")
            .build();
    }

    public DurationRequestDTO generateExistedDuration() {
        return DurationRequestDTO.builder()
            .id(1)
            .startTime("09:00")
            .endTime("10:20")
            .build();
    }

    public DurationRequestDTO generateUpdatedDuration() {
        return DurationRequestDTO.builder()
            .id(0)
            .startTime("09:10")
            .endTime("10:30")
            .build();
    }

    public DurationResponseDTO generateDurationForFindById() {
        return DurationResponseDTO.builder()
            .id(1)
            .startTime("09:00")
            .endTime("10:20")
            .build();
    }

    public DurationRequestDTO generateInvalidDuration() {
        return DurationRequestDTO.builder()
            .id(0)
            .startTime("")
            .endTime("10:30")
            .build();
    }
}
