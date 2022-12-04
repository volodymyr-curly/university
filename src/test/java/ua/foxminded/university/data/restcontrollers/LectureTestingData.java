package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.dto.duration.DurationNestedDTO;
import ua.foxminded.university.dto.duration.DurationRequestDTO;
import ua.foxminded.university.dto.group.GroupNestedDTO;
import ua.foxminded.university.dto.lecture.LectureRequestDTO;
import ua.foxminded.university.dto.room.RoomNestedDTO;
import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;
import ua.foxminded.university.dto.teacher.TeacherNestedDTO;

import java.time.LocalDate;
import java.util.Collections;

public class LectureTestingData {

    public static final String SHOW_ALL_URL = "/api/lectures";
    public static final String SHOW_BY_SUBJECT_URL = "/api/lectures/subject/list";
    public static final String SHOW_BY_TEACHER_URL = "/api/lectures/teacher/list";
    public static final String SHOW_BY_GROUP_URL = "/api/lectures/group/list";
    public static final String SHOW_BY_ROOM_URL = "/api/lectures/room/list";
    public static final String SHOW_BY_DURATION_URL = "/api/lectures/duration/list";
    public static final String UPDATE_URL = "/api/lectures/{id}";
    public static final String EDIT_URL = "/api/lectures/{id}/edit";
    public static final String DELETE_URL = "/api/lectures/{id}/delete";

    public static final String NOT_FOUND_MESSAGE = "Lecture not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";
    public static final String EXISTS_MESSAGE = "Lecture already exists";

    public static final int LIST_SIZE = 5;
    public static final int LIST_BY_SUBJECT_SIZE = 2;
    public static final int LIST_BY_TEACHER_SIZE = 2;
    public static final int LIST_BY_GROUP_SIZE = 2;
    public static final int LIST_BY_ROOM_SIZE = 2;
    public static final int LIST_BY_DURATION_SIZE = 2;
    public static final int ID = 3;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 1;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"date\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public LectureRequestDTO generateLectureForCreate() {
        return LectureRequestDTO.builder()
            .id(0)
            .date(LocalDate.of(2022, 12, 2))
            .subject(SubjectNestedDTO.builder().id(5).build())
            .duration(DurationNestedDTO.builder().id(1).build())
            .groups(Collections.singletonList(GroupNestedDTO.builder().id(4).build()))
            .room(RoomNestedDTO.builder().id(1).build())
            .teacher(TeacherNestedDTO.builder().id(3).build())
            .build();
    }

    public LectureRequestDTO generateLectureForUpdate() {
        return LectureRequestDTO.builder()
            .id(3)
            .date(LocalDate.of(2022, 12, 1))
            .subject(SubjectNestedDTO.builder().id(3).name("Medical Chemistry").build())
            .teacher(TeacherNestedDTO.builder().id(2).employeeLastName("Tarasenko").build())
            .room(RoomNestedDTO.builder().id(3).build())
            .duration(DurationNestedDTO.builder().id(3).build())
            .build();
    }

    public LectureRequestDTO generateUpdatedLecture() {
        return LectureRequestDTO.builder()
            .id(0)
            .date(LocalDate.of(2022, 12, 1))
            .subject(SubjectNestedDTO.builder().id(3).build())
            .teacher(TeacherNestedDTO.builder().id(2).build())
            .room(RoomNestedDTO.builder().id(3).build())
            .duration(DurationNestedDTO.builder().id(3).build())
            .build();
    }

    public LectureRequestDTO generateInvalidLecture() {
        return LectureRequestDTO.builder()
            .id(0)
            .date(null)
            .subject(SubjectNestedDTO.builder().id(5).build())
            .duration(DurationNestedDTO.builder().id(1).build())
            .groups(Collections.singletonList(GroupNestedDTO.builder().id(4).build()))
            .room(RoomNestedDTO.builder().id(1).build())
            .teacher(TeacherNestedDTO.builder().id(3).build())
            .build();
    }

    public LectureRequestDTO generateExistedLecture() {
        return LectureRequestDTO.builder()
            .id(0)
            .date(LocalDate.of(2022, 12, 1))
            .subject(SubjectNestedDTO.builder().id(3).build())
            .teacher(TeacherNestedDTO.builder().id(2).build())
            .room(RoomNestedDTO.builder().id(3).build())
            .duration(DurationNestedDTO.builder().id(3).build())
            .build();
    }
}
