package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.dto.group.GroupResponseDTO;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;

import java.util.Collections;

public class GroupTestingData {

    public static final String SHOW_ALL_URL = "/api/groups";
    public static final String SHOW_BY_DEPARTMENT_URL = "/api/groups/department/list";
    public static final String SHOW_BY_SUBJECT_URL = "/api/groups/subject/list";
    public static final String SHOW_BY_LECTURE_URL = "/api/groups/lecture/list";
    public static final String SHOW_URL = "/api/groups/{id}";
    public static final String EDIT_URL = "/api/groups/{id}/edit";
    public static final String DELETE_URL = "/api/groups/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "Group not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";

    public static final int LIST_SIZE = 4;
    public static final int LIST_BY_DEPARTMENT_SIZE = 3;
    public static final int LIST_BY_SUBJECT_SIZE = 3;
    public static final int LIST_BY_LECTURE_SIZE = 3;
    public static final int ID = 4;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 2;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"departmentId\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public final static String EXISTS_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"Global error\"," +
        "\"rejectedValue\":\"groupRequestDTO\",\"messageError\":\"Group is already exist\"}]}";

    public GroupRequestDTO generateGroupForCreate() {
        return GroupRequestDTO.builder()
            .id(0)
            .name("Fin_f-2")
            .departmentId(3)
            .subjects(Collections.singletonList(SubjectNestedDTO.builder().id(5).build()))
            .build();
    }

    public GroupRequestDTO generateGroupForUpdate() {
        return GroupRequestDTO.builder()
            .id(4)
            .name("Fin_f-1")
            .departmentId(3)
            .departmentName("Finance")
            .build();
    }

    public GroupRequestDTO generateUpdatedGroup() {
        return GroupRequestDTO.builder()
            .id(0)
            .name("Fin_f-1")
            .departmentId(3)
            .subjects(Collections.singletonList(SubjectNestedDTO.builder().id(5).build()))
            .lectures(Collections.singletonList(LectureResponseDTO.builder().id(5).build()))
            .build();
    }

    public GroupResponseDTO generateGroupForFindById() {
        return GroupResponseDTO.builder()
            .id(4)
            .name("Fin_f-1")
            .departmentName("Finance")
            .build();
    }

    public GroupRequestDTO generateInvalidGroup() {
        return GroupRequestDTO.builder()
            .id(0)
            .name("Fin_f-2")
            .departmentName(null)
            .build();
    }

    public GroupRequestDTO generateExistedGroup() {
        return GroupRequestDTO.builder()
            .id(0)
            .name("Fin_f-1")
            .departmentId(3)
            .build();
    }
}
