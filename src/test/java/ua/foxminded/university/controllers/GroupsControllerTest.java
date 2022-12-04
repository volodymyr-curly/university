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
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.domain.Group;
import ua.foxminded.university.domain.Lecture;
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.DepartmentService;
import ua.foxminded.university.service.interfaces.GroupService;
import ua.foxminded.university.service.interfaces.LectureService;
import ua.foxminded.university.service.interfaces.SubjectService;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(MockitoExtension.class)
class GroupsControllerTest {

    private static final String GROUPS_URL = "/groups";
    private static final String GROUPS_BY_DEPARTMENT_URL = "/groups/department/list/?id=1";
    private static final String GROUPS_BY_SUBJECT_URL = "/groups/subject/list/?id=1";
    private static final String GROUPS_BY_LECTURE_URL = "/groups/lecture/list/?id=1";
    private static final String GROUP_URL = "/groups/1";
    private static final String SAVE_URL = "/groups/save";
    private static final String EDIT_URL = "/groups/1/edit";
    private static final String DELETE_URL = "/groups/1/delete";

    private static final String ALL_VIEW = "groups/all";
    private static final String BY_ID_VIEW = "groups/by-id";
    private static final String SHOW_VIEW = "groups/show";
    private static final String EDIT_VIEW = "groups/edit";
    private static final String REDIRECT = "redirect:/groups";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String GROUP_ATTRIBUTE = "group";
    private static final String GROUPS_ATTRIBUTE = "groups";
    private static final String DEPARTMENTS_ATTRIBUTE = "departments";
    private static final String NEW_DEPARTMENT_ATTRIBUTE = "department";
    private static final String SUBJECTS_ATTRIBUTE = "subjects";
    private static final String NEW_SUBJECT_ATTRIBUTE = "subject";
    private static final String LECTURES_ATTRIBUTE = "lectures";
    private static final String NEW_LECTURE_ATTRIBUTE = "lecture";

    private static final int ACTUAL_ID = 1;

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private LectureService lectureService;

    @InjectMocks
    private GroupsController groupsController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindGroups_thenAllView() throws Exception {
        when(groupService.findAll()).thenReturn(setExpectedGroups());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(lectureService.findAll()).thenReturn(setExpectedLectures());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_DEPARTMENT_ATTRIBUTE, new Department()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()));
    }

    @Test
    void whenFindGroups_thenServiceException() throws Exception {
        when(groupService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindGroups_thenServerError() throws Exception {
        when(groupService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindGroupsByDepartmentId_thenByDepartmentIdView() throws Exception {
        when(groupService.findByDepartment(ACTUAL_ID)).thenReturn(setExpectedGroups());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(lectureService.findAll()).thenReturn(setExpectedLectures());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_DEPARTMENT_ATTRIBUTE, new Department()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()));
    }

    @Test
    void whenFindGroupsByDepartmentId_thenServiceException() throws Exception {
        when(groupService.findByDepartment(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindGroupsByDepartmentId_thenServerError() throws Exception {
        when(groupService.findByDepartment(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindGroupsBySubjectId_thenBySubjectIdView() throws Exception {
        when(groupService.findBySubject(ACTUAL_ID)).thenReturn(setExpectedGroups());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(lectureService.findAll()).thenReturn(setExpectedLectures());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_DEPARTMENT_ATTRIBUTE, new Department()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()));
    }

    @Test
    void whenFindGroupsBySubjectId_thenServiceException() throws Exception {
        when(groupService.findBySubject(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindGroupsBySubjectId_thenServerError() throws Exception {
        when(groupService.findBySubject(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindGroupsByLectureId_thenByLecturedView() throws Exception {
        when(groupService.findByLecture(ACTUAL_ID)).thenReturn(setExpectedGroups());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(lectureService.findAll()).thenReturn(setExpectedLectures());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_LECTURE_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_DEPARTMENT_ATTRIBUTE, new Department()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_LECTURE_ATTRIBUTE, new Lecture()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()));
    }

    @Test
    void whenFindGroupsByLectureId_thenServiceException() throws Exception {
        when(groupService.findByLecture(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_LECTURE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindGroupsByLectureId_thenServerError() throws Exception {
        when(groupService.findByLecture(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUPS_BY_LECTURE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindGroup_thenShowView() throws Exception {
        when(groupService.find(setExpectedGroups().get(0).getId()))
            .thenReturn(setExpectedGroups().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(SHOW_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUP_ATTRIBUTE, setExpectedGroups().get(0)));
    }

    @Test
    void whenFindGroup_thenServiceException() throws Exception {
        when(groupService.find(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindGroup_thenServerError() throws Exception {
        when(groupService.find(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewGroup_thenNewGroupInRedirectView() throws Exception {
        ArgumentCaptor<Group> groupCapture = ArgumentCaptor.forClass(Group.class);
        doNothing().when(groupService).add(groupCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/groups"));
    }

    @Test
    void whenSaveNewGroup_thenServiceException() throws Exception {
        ArgumentCaptor<Group> groupCapture = ArgumentCaptor.forClass(Group.class);
        doThrow(new ServiceException()).when(groupService).add(groupCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewGroup_thenServerError() throws Exception {
        ArgumentCaptor<Group> groupCapture = ArgumentCaptor.forClass(Group.class);
        doThrow(new RuntimeException()).when(groupService).add(groupCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindGroupToUpdate_thenEditView() throws Exception {
        when(groupService.find(setExpectedGroups().get(0).getId()))
            .thenReturn(setExpectedGroups().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(EDIT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUP_ATTRIBUTE, setExpectedGroups().get(0)));
    }

    @Test
    void whenFindGroupToUpdate_thenServiceException() throws Exception {
        when(groupService.find(ACTUAL_ID))
            .thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindGroupToUpdate_thenServerError() throws Exception {
        when(groupService.find(ACTUAL_ID))
            .thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateGroup_thenUpdatedGroupRedirectView() throws Exception {
        doNothing().when(groupService).update(ACTUAL_ID, setExpectedGroups().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(GROUP_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE));
    }

    @Test
    void whenUpdateGroup_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(groupService).update(ACTUAL_ID, setExpectedGroups().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateGroup_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(groupService).update(ACTUAL_ID, setExpectedGroups().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteGroup_thenNoGroupInRedirectView() throws Exception {
        doNothing().when(groupService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));
    }

    @Test
    void whenDeleteGroup_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(groupService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteGroup_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(groupService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
