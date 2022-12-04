package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.dto.subject.SubjectRequestDTO;
import ua.foxminded.university.dto.group.GroupNestedDTO;
import ua.foxminded.university.dto.teacher.TeacherNestedDTO;

import java.time.LocalDate;
import java.util.Collections;

public class SubjectTestingData {

    public static final String SHOW_ALL_URL = "/api/subjects";
    public static final String SHOW_BY_TEACHER_URL = "/api/subjects/teacher/list";
    public static final String SHOW_BY_GROUP_URL = "/api/subjects/group/list";
    public static final String UPDATE_URL = "/api/subjects/{id}";
    public static final String EDIT_URL = "/api/subjects/{id}/edit";
    public static final String DELETE_URL = "/api/subjects/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "Subject not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";
    public static final String EXISTS_MESSAGE = "Subject already exists";

    public static final int LIST_SIZE = 5;
    public static final int LIST_BY_TEACHER_SIZE = 2;
    public static final int LIST_BY_GROUP_SIZE = 2;
    public static final int ID = 4;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 1;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"startDate\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public SubjectRequestDTO generateSubjectForCreate() {
        return SubjectRequestDTO.builder()
            .id(0)
            .name("Geometry")
            .startDate(LocalDate.of(2022, 12, 1))
            .endDate(LocalDate.of(2022, 12, 31))
            .groups(Collections.singletonList(GroupNestedDTO.builder().id(1).build()))
            .teachers(Collections.singletonList(TeacherNestedDTO.builder().id(1).build()))
            .build();
    }

    public SubjectRequestDTO generateSubjectForUpdate() {
        return SubjectRequestDTO.builder()
            .id(4)
            .name("Finance And Credit")
            .startDate(LocalDate.of(2022, 9, 1))
            .endDate(LocalDate.of(2022, 12, 31))
            .build();
    }

    public SubjectRequestDTO generateUpdatedSubject() {
        return SubjectRequestDTO.builder()
            .id(0)
            .name("Finance And Credit")
            .startDate(LocalDate.of(2022, 9, 1))
            .endDate(LocalDate.of(2022, 12, 31))
            .groups(Collections.singletonList(GroupNestedDTO.builder().id(4).build()))
            .teachers(Collections.singletonList(TeacherNestedDTO.builder().id(3).build()))
            .build();
    }

    public SubjectRequestDTO generateInvalidSubject() {
        return SubjectRequestDTO.builder()
            .id(0)
            .name("Geometry")
            .startDate(null)
            .endDate(LocalDate.of(2022, 12, 31))
            .groups(Collections.singletonList(GroupNestedDTO.builder().id(1).build()))
            .teachers(Collections.singletonList(TeacherNestedDTO.builder().id(1).build()))
            .build();
    }

    public SubjectRequestDTO generateExistedSubject() {
        return SubjectRequestDTO.builder()
            .id(0)
            .name("Finance And Credit")
            .startDate(LocalDate.of(2022, 9, 1))
            .endDate(LocalDate.of(2022, 12, 31))
            .groups(Collections.singletonList(GroupNestedDTO.builder().id(4).build()))
            .teachers(Collections.singletonList(TeacherNestedDTO.builder().id(3).build()))
            .build();
    }
}
