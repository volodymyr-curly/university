package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.domain.MarkValue;
import ua.foxminded.university.dto.mark.MarkRequestDTO;
import ua.foxminded.university.dto.student.StudentNestedDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;

public class MarkTestingData {

    public static final String SHOW_ALL_URL = "/api/marks";
    public static final String SHOW_BY_SUBJECT_URL = "/api/marks/subject/list";
    public static final String SHOW_BY_STUDENT_URL = "/api/marks/subject/list";
    public static final String UPDATE_URL = "/api/marks/{id}";
    public static final String EDIT_URL = "/api/marks/{id}/edit";
    public static final String DELETE_URL = "/api/marks/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "Mark not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";
    public static final String EXISTS_MESSAGE = "Mark already exists";

    public static final int LIST_SIZE = 10;
    public static final int LIST_BY_SUBJECT_SIZE = 3;
    public static final int LIST_BY_STUDENT_SIZE = 3;
    public static final int ID = 1;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 20;
    public static final int ID_TO_UPDATE = 2;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"value\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public MarkRequestDTO generateMarkForCreate() {
        return MarkRequestDTO.builder()
            .id(0)
            .value(MarkValue.E)
            .student(StudentNestedDTO.builder().id(1).build())
            .subject(SubjectNestedDTO.builder().id(5).build())
            .build();
    }

    public MarkRequestDTO generateMarkForUpdate() {
        return MarkRequestDTO.builder()
            .id(0)
            .value(MarkValue.A)
            .student(StudentNestedDTO.builder()
                .id(1)
                .lastName("Ivanenko")
                .build())
            .subject(SubjectNestedDTO.builder()
                .id(1)
                .name("Chemistry")
                .build())
            .build();
    }

    public MarkRequestDTO generateUpdatedMark() {
        return MarkRequestDTO.builder()
            .id(0)
            .value(MarkValue.A)
            .student(StudentNestedDTO.builder().id(1).build())
            .subject(SubjectNestedDTO.builder().id(1).build())
            .build();
    }

    public MarkRequestDTO generateInvalidMark() {
        return MarkRequestDTO.builder()
            .id(0)
            .value(null)
            .student(StudentNestedDTO.builder().id(1).build())
            .subject(SubjectNestedDTO.builder().id(1).build())
            .build();
    }

    public MarkRequestDTO generateExistedMark() {
        return MarkRequestDTO.builder()
            .id(0)
            .value(MarkValue.A)
            .student(StudentNestedDTO.builder().id(1).build())
            .subject(SubjectNestedDTO.builder().id(1).build())
            .build();
    }
}
