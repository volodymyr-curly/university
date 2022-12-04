package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.dto.faculty.FacultyRequestDTO;

public class FacultyTestingData {

    public static final String SHOW_ALL_URL = "/api/faculties";
    public static final String UPDATE_URL = "/api/faculties/{id}";
    public static final String EDIT_URL = "/api/faculties/{id}/edit";
    public static final String DELETE_URL = "/api/faculties/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "Faculty not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";

    public static final int LIST_SIZE = 2;
    public static final int ID = 1;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 2;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"name\"," +
        "\"rejectedValue\":\"geometry\",\"messageError\":\"Should be in this format: Abcdefg, Abcdefg Hijklmn, etc.\"}]}";
    public final static String EXISTS_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"Global error\"," +
        "\"rejectedValue\":\"facultyRequestDTO\",\"messageError\":\"Faculty is already exist\"}]}";

    public FacultyRequestDTO generateFacultyForCreate() {
        return FacultyRequestDTO.builder()
            .id(0)
            .name("Geometry")
            .build();
    }

    public FacultyRequestDTO generateFacultyForUpdate() {
        return FacultyRequestDTO.builder()
            .id(1)
            .name("Chemistry")
            .build();
    }

    public FacultyRequestDTO generateUpdatedFaculty() {
        return FacultyRequestDTO.builder()
            .id(0)
            .name("Chemistry")
            .build();
    }

    public FacultyRequestDTO generateInvalidFaculty() {
        return FacultyRequestDTO.builder()
            .id(0)
            .name("geometry")
            .build();
    }

    public FacultyRequestDTO generateExistedFaculty() {
        return FacultyRequestDTO.builder()
            .id(0)
            .name("Chemistry")
            .build();
    }
}
