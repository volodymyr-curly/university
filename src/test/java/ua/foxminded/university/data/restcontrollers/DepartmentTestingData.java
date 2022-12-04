package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.dto.department.DepartmentResponseDTO;

public class DepartmentTestingData {

    public static final String SHOW_ALL_URL = "/api/departments";
    public static final String SHOW_BY_FACULTY_URL = "/api/departments/faculty/list";
    public static final String SHOW_URL = "/api/departments/{id}";
    public static final String EDIT_URL = "/api/departments/{id}/edit";
    public static final String DELETE_URL = "/api/departments/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "Department not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";

    public static final int LIST_SIZE = 3;
    public static final int LIST_BY_FACULTY_SIZE = 2;
    public static final int ID = 1;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 2;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"facultyId\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public final static String EXISTS_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"Global error\"," +
        "\"rejectedValue\":\"departmentRequestDTO\",\"messageError\":\"Department is already exist\"}]}";

    public DepartmentRequestDTO generateDepartmentForCreate() {
        return DepartmentRequestDTO.builder()
            .id(0)
            .name("Geometry")
            .facultyId(1)
            .build();
    }

    public DepartmentRequestDTO generateDepartmentForUpdate() {
        return DepartmentRequestDTO.builder()
            .id(1)
            .name("Organic Chemistry")

            .facultyId(1)
            .facultyName("Chemistry")
            .build();
    }

    public DepartmentRequestDTO generateUpdatedDepartment() {
        return DepartmentRequestDTO.builder()
            .id(0)
            .name("Organic Chemistry")
            .facultyId(1)
            .build();
    }

    public DepartmentResponseDTO generateDepartmentForFindById() {
        return DepartmentResponseDTO.builder()
            .id(1)
            .name("Organic Chemistry")
            .facultyName("Chemistry")
            .build();
    }

    public DepartmentRequestDTO generateInvalidDepartment() {
        return DepartmentRequestDTO.builder()
            .id(1)
            .name("Geometry")
            .facultyName("Chemistry")
            .build();
    }
}
