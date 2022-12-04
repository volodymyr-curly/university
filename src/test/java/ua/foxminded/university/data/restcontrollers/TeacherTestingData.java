package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.domain.Degree;
import ua.foxminded.university.dto.employee.EmployeeDTO;
import ua.foxminded.university.dto.employee.EmployeeNestedDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;
import ua.foxminded.university.dto.teacher.TeacherRequestDTO;
import ua.foxminded.university.dto.teacher.TeacherResponseDTO;

import java.util.Collections;

public class TeacherTestingData {

    public static final String SHOW_ALL_URL = "/api/teachers";
    public static final String SHOW_BY_DEPARTMENT_URL = "/api/teachers/department/list";
    public static final String SHOW_BY_SUBJECT_URL = "/api/teachers/subject/list";
    public static final String SHOW_URL = "/api/teachers/{id}";
    public static final String EDIT_URL = "/api/teachers/{id}/edit";
    public static final String DELETE_URL = "/api/teachers/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "Teacher not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";

    public static final int LIST_SIZE = 3;
    public static final int LIST_BY_DEPARTMENT_SIZE = 2;
    public static final int LIST_BY_SUBJECT_SIZE = 2;
    public static final int ID = 2;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 3;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"degree\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public final static String EXISTS_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"Global error\"," +
        "\"rejectedValue\":\"teacherRequestDTO\",\"messageError\":\"Teacher is already exist\"}]}";

    public TeacherRequestDTO generateTeacherForCreate() {
        return TeacherRequestDTO.builder()
            .id(0)
            .employee(EmployeeNestedDTO.builder().id(8).build())
            .degree(Degree.ASSOCIATE)
            .subjects(Collections.singletonList
                (SubjectNestedDTO.builder().id(4).build()))
            .build();
    }

    public TeacherRequestDTO generateTeacherForUpdate() {
        return TeacherRequestDTO.builder()
            .id(2)
            .employee(EmployeeNestedDTO.builder()
                .id(6)
                .lastName("Tarasenko")
                .firstName("Taras")
                .build())
            .degree(Degree.DOCTOR)
            .build();
    }

    public TeacherRequestDTO generateUpdatedTeacher() {
        return TeacherRequestDTO.builder()
            .id(0)
            .employee(EmployeeNestedDTO.builder().id(6).build())
            .degree(Degree.MASTER)
            .subjects(Collections.singletonList(SubjectNestedDTO.builder().id(1).build()))
            .build();
    }

    public TeacherResponseDTO generateTeacherForFindById() {
        return TeacherResponseDTO.builder()
            .id(2)
            .employee(EmployeeDTO.builder()
                .firstName("Taras")
                .lastName("Tarasenko")
                .build())
            .degree(Degree.DOCTOR)
            .build();
    }

    public TeacherRequestDTO generateInvalidTeacher() {
        return TeacherRequestDTO.builder()
            .id(0)
            .employee(EmployeeNestedDTO.builder().id(6).build())
            .degree(null)
            .subjects(Collections.singletonList(SubjectNestedDTO.builder().id(1).build()))
            .build();
    }

    public TeacherRequestDTO generateExistedTeacher() {
        return TeacherRequestDTO.builder()
            .id(0)
            .employee(EmployeeNestedDTO.builder().id(6).build())
            .degree(Degree.MASTER)
            .subjects(Collections.singletonList(SubjectNestedDTO.builder().id(1).build()))
            .build();
    }
}
