package ua.foxminded.university.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.foxminded.university.domain.*;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.*;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(MockitoExtension.class)
class LecturesControllerTest {

    private static final String LECTURES_URL = "/lectures";
    private static final String LECTURES_BY_DURATION_URL = "/lectures/duration/list/?id=1";
    private static final String LECTURES_BY_ROOM_URL = "/lectures/room/list/?number=1";
    private static final String LECTURES_BY_SUBJECT_URL = "/lectures/subject/list/?id=1";
    private static final String LECTURES_BY_GROUP_URL = "/lectures/group/list/?id=1";
    private static final String LECTURES_BY_TEACHER_URL = "/lectures/teacher/list/?id=5";
    private static final String LECTURE_URL = "/lectures/1";
    private static final String SAVE_URL = "/lectures/save";
    private static final String EDIT_URL = "/lectures/1/edit";
    private static final String DELETE_URL = "/lectures/1/delete";

    private static final String ALL_VIEW = "lectures/all";
    private static final String BY_ID_VIEW = "lectures/by-id";
    private static final String EDIT_VIEW = "lectures/edit";
    private static final String REDIRECT = "redirect:/lectures";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String LECTURES_ATTRIBUTE = "lectures";
    private static final String LECTURE_ATTRIBUTE = "lecture";
    private static final String DURATIONS_ATTRIBUTE = "durations";
    private static final String DURATION_ATTRIBUTE = "duration";
    private static final String ROOMS_ATTRIBUTE = "rooms";
    private static final String ROOM_ATTRIBUTE = "room";
    private static final String SUBJECTS_ATTRIBUTE = "subjects";
    private static final String SUBJECT_ATTRIBUTE = "subject";
    private static final String GROUPS_ATTRIBUTE = "groups";
    private static final String GROUP_ATTRIBUTE = "group";
    private static final String TEACHERS_ATTRIBUTE = "teachers";
    private static final String TEACHER_ATTRIBUTE = "teacher";

    private static final int ACTUAL_ID = 1;
    private static final int TEACHER_ID = 5;

    private MockMvc mockMvc;

    @Mock
    private LectureService lectureService;

    @Mock
    private DurationService durationService;

    @Mock
    private LectureRoomService roomService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private GroupService groupService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private LecturesController lecturesController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(lecturesController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindLectures_thenAllView() throws Exception {
        when(lectureService.findAll()).thenReturn(setExpectedLectures());
        mockLectureController();

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATION_ATTRIBUTE, new Duration()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATIONS_ATTRIBUTE, setExpectedDurations()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOM_ATTRIBUTE, new LectureRoom()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOMS_ATTRIBUTE, setExpectedRooms()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindLectures_thenServiceException() throws Exception {
        when(lectureService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindLectures_thenServerError() throws Exception {
        when(lectureService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindByDurationId_thenByIdView() throws Exception {
        when(lectureService.findByDuration(ACTUAL_ID)).thenReturn(setExpectedLectures());
        mockLectureController();

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_DURATION_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATION_ATTRIBUTE, new Duration()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATIONS_ATTRIBUTE, setExpectedDurations()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOM_ATTRIBUTE, new LectureRoom()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOMS_ATTRIBUTE, setExpectedRooms()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindByDurationId_thenServiceException() throws Exception {
        when(lectureService.findByDuration(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_DURATION_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindByDurationId_thenServerError() throws Exception {
        when(lectureService.findByDuration(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_DURATION_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindByRoomNumber_thenByIdView() throws Exception {
        when(lectureService.findByRoom(ACTUAL_ID)).thenReturn(setExpectedLectures());
        mockLectureController();

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_ROOM_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATION_ATTRIBUTE, new Duration()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATIONS_ATTRIBUTE, setExpectedDurations()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOM_ATTRIBUTE, new LectureRoom()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOMS_ATTRIBUTE, setExpectedRooms()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindByRoomNumber_thenServiceException() throws Exception {
        when(lectureService.findByRoom(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_ROOM_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindByRoomNumber_thenServerError() throws Exception {
        when(lectureService.findByRoom(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_ROOM_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindBySubjectId_thenByIdView() throws Exception {
        when(lectureService.findBySubject(ACTUAL_ID)).thenReturn(setExpectedLectures());
        mockLectureController();

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATION_ATTRIBUTE, new Duration()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATIONS_ATTRIBUTE, setExpectedDurations()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOM_ATTRIBUTE, new LectureRoom()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOMS_ATTRIBUTE, setExpectedRooms()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindBySubjectId_thenServiceException() throws Exception {
        when(lectureService.findBySubject(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindBySubjectId_thenServerError() throws Exception {
        when(lectureService.findBySubject(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }


    @Test
    void whenFindByGroupId_thenByIdView() throws Exception {
        when(lectureService.findByGroup(ACTUAL_ID)).thenReturn(setExpectedLectures());
        mockLectureController();

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATION_ATTRIBUTE, new Duration()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATIONS_ATTRIBUTE, setExpectedDurations()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOM_ATTRIBUTE, new LectureRoom()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOMS_ATTRIBUTE, setExpectedRooms()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindByGroupId_thenServiceException() throws Exception {
        when(lectureService.findByGroup(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindByGroupId_thenServerError() throws Exception {
        when(lectureService.findByGroup(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindByTeacherId_thenByIdView() throws Exception {
        when(lectureService.findByTeacher(TEACHER_ID)).thenReturn(setExpectedLectures());
        mockLectureController();

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATION_ATTRIBUTE, new Duration()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATIONS_ATTRIBUTE, setExpectedDurations()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOM_ATTRIBUTE, new LectureRoom()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOMS_ATTRIBUTE, setExpectedRooms()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindByTeacherId_thenServiceException() throws Exception {
        when(lectureService.findByTeacher(TEACHER_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindByTeacherId_thenServerError() throws Exception {
        when(lectureService.findByTeacher(TEACHER_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(LECTURES_BY_TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewLecture_thenNewLectureInRedirectView() throws Exception {
        ArgumentCaptor<Lecture> lectureCapture = ArgumentCaptor.forClass(Lecture.class);
        doNothing().when(lectureService).add(lectureCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/lectures"));
    }

    @Test
    void whenSaveNewLecture_thenServiceException() throws Exception {
        ArgumentCaptor<Lecture> lectureCapture = ArgumentCaptor.forClass(Lecture.class);
        doThrow(new ServiceException()).when(lectureService).add(lectureCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post("/lectures/save"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewLecture_thenServerError() throws Exception {
        ArgumentCaptor<Lecture> lectureCapture = ArgumentCaptor.forClass(Lecture.class);
        doThrow(new RuntimeException()).when(lectureService).add(lectureCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindLectureToUpdate_thenEditView() throws Exception {
        when(lectureService.find(setExpectedLectures().get(0).getId()))
            .thenReturn(setExpectedLectures().get(0));
        mockLectureController();

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(EDIT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURE_ATTRIBUTE, setExpectedLectures().get(0)))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATIONS_ATTRIBUTE, setExpectedDurations()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOMS_ATTRIBUTE, setExpectedRooms()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindLectureToUpdate_thenServiceException() throws Exception {
        when(lectureService.find(ACTUAL_ID))
            .thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindLectureToUpdate_thenServerError() throws Exception {
        when(lectureService.find(ACTUAL_ID))
            .thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateLecture_thenUpdatedLectureRedirectView() throws Exception {
        doNothing().when(lectureService).update(ACTUAL_ID, setExpectedLectures().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(LECTURE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(LECTURE_ATTRIBUTE));
    }

    @Test
    void whenUpdateLecture_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(lectureService).update(ACTUAL_ID, setExpectedLectures().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(LECTURE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateLecture_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(lectureService).update(ACTUAL_ID, setExpectedLectures().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(LECTURE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteLecture_thenNoLectureInRedirectView() throws Exception {
        doNothing().when(lectureService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));
    }

    @Test
    void whenDeleteLecture_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(lectureService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteLecture_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(lectureService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    private void mockLectureController() {
        when(durationService.findAll()).thenReturn(setExpectedDurations());
        when(roomService.findAll()).thenReturn(setExpectedRooms());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(groupService.findAll()).thenReturn(setExpectedGroups());
        when(teacherService.findAll()).thenReturn(setExpectedTeachers());
    }
}
