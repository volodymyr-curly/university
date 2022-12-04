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
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.domain.Teacher;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.GroupService;
import ua.foxminded.university.service.interfaces.LectureService;
import ua.foxminded.university.service.interfaces.SubjectService;
import ua.foxminded.university.service.interfaces.TeacherService;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(MockitoExtension.class)
class SubjectsControllerTest {

    private static final String SUBJECTS_URL = "/subjects";
    private static final String SUBJECTS_BY_GROUP_URL = "/subjects/group/list/?id=1";
    private static final String SUBJECTS_BY_TEACHER_URL = "/subjects/teacher/list/?id=1";
    private static final String SUBJECT_URL = "/subjects/1";
    private static final String SAVE_URL = "/subjects/save";
    private static final String EDIT_URL = "/subjects/1/edit";
    private static final String DELETE_URL = "/subjects/1/delete";

    private static final String ALL_VIEW = "subjects/all";
    private static final String BY_ID_VIEW = "subjects/by-id";
    private static final String SHOW_VIEW = "subjects/show";
    private static final String EDIT_VIEW = "subjects/edit";
    private static final String REDIRECT = "redirect:/subjects";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String SUBJECTS_ATTRIBUTE = "subjects";
    private static final String SUBJECT_ATTRIBUTE = "subject";
    private static final String GROUPS_ATTRIBUTE = "groups";
    private static final String GROUP_ATTRIBUTE = "group";
    private static final String TEACHERS_ATTRIBUTE = "teachers";
    private static final String TEACHER_ATTRIBUTE = "teacher";
    private static final String LECTURES_ATTRIBUTE = "lectures";

    private static final int ACTUAL_ID = 1;

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;

    @Mock
    private GroupService groupService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private LectureService lectureService;

    @InjectMocks
    private SubjectsController subjectsController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectsController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindSubjects_thenAllView() throws Exception {
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(groupService.findAll()).thenReturn(setExpectedGroups());
        when(teacherService.findAll()).thenReturn(setExpectedTeachers());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindSubjects_thenServiceException() throws Exception {
        when(subjectService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindSubjects_thenServerError() throws Exception {
        when(subjectService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindSubjectsByGroupId_thenByGroupIdView() throws Exception {
        when(subjectService.findByGroup(ACTUAL_ID)).thenReturn(setExpectedSubjects());
        when(groupService.findAll()).thenReturn(setExpectedGroups());
        when(teacherService.findAll()).thenReturn(setExpectedTeachers());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindSubjectsByGroupId_thenServiceException() throws Exception {
        when(subjectService.findByGroup(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindSubjectsByGroupId_thenServerError() throws Exception {
        when(subjectService.findByGroup(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindSubjectsByTeacherId_thenByTeacherIdView() throws Exception {
        when(subjectService.findByTeacher(ACTUAL_ID)).thenReturn(setExpectedSubjects());
        when(groupService.findAll()).thenReturn(setExpectedGroups());
        when(teacherService.findAll()).thenReturn(setExpectedTeachers());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_BY_TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindSubjectsByTeacherId_thenServiceException() throws Exception {
        when(subjectService.findByTeacher(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_BY_TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindSubjectsByTeacherId_thenServerError() throws Exception {
        when(subjectService.findByTeacher(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECTS_BY_TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindSubject_thenShowView() throws Exception {
        when(subjectService.find(setExpectedSubjects().get(0).getId()))
            .thenReturn(setExpectedSubjects().get(0));
        when(teacherService.findBySubject(ACTUAL_ID)).thenReturn(setExpectedTeachers());
        when(lectureService.findBySubject(ACTUAL_ID)).thenReturn(setExpectedLectures());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(SHOW_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, setExpectedSubjects().get(0)))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()));
    }

    @Test
    void whenFindSubject_thenServiceException() throws Exception {
        when(subjectService.find(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindSubject_thenServerError() throws Exception {
        when(subjectService.find(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewSubject_thenNewSubjectInRedirectView() throws Exception {
        ArgumentCaptor<Subject> subjectCapture = ArgumentCaptor.forClass(Subject.class);
        doNothing().when(subjectService).add(subjectCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/subjects"));
    }

    @Test
    void whenSaveNewSubject_thenServiceException() throws Exception {
        ArgumentCaptor<Subject> subjectCapture = ArgumentCaptor.forClass(Subject.class);
        doThrow(new ServiceException()).when(subjectService).add(subjectCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post("/subjects/save"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewSubject_thenServerError() throws Exception {
        ArgumentCaptor<Subject> subjectCapture = ArgumentCaptor.forClass(Subject.class);
        doThrow(new RuntimeException()).when(subjectService).add(subjectCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindSubjectToUpdate_thenEditView() throws Exception {
        when(subjectService.find(setExpectedSubjects().get(0).getId()))
            .thenReturn(setExpectedSubjects().get(0));
        when(groupService.findAll()).thenReturn(setExpectedGroups());
        when(teacherService.findAll()).thenReturn(setExpectedTeachers());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(EDIT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, setExpectedSubjects().get(0)))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()));
    }

    @Test
    void whenFindSubjectToUpdate_thenServiceException() throws Exception {
        when(subjectService.find(ACTUAL_ID))
            .thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindSubjectToUpdate_thenServerError() throws Exception {
        when(subjectService.find(ACTUAL_ID))
            .thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateSubject_thenUpdatedSubjectRedirectView() throws Exception {
        doNothing().when(subjectService).update(ACTUAL_ID, setExpectedSubjects().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(SUBJECT_ATTRIBUTE));
    }

    @Test
    void whenUpdateSubject_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(subjectService).update(ACTUAL_ID, setExpectedSubjects().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateSubject_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(subjectService).update(ACTUAL_ID, setExpectedSubjects().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteSubject_thenNoSubjectInRedirectView() throws Exception {
        doNothing().when(subjectService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));
    }

    @Test
    void whenDeleteSubject_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(subjectService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteSubject_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(subjectService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
