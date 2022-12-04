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
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.domain.Teacher;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.*;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    private static final String TEACHERS_URL = "/teachers";
    private static final String TEACHERS_BY_DEPARTMENT_URL = "/teachers/department/list/?id=1";
    private static final String TEACHERS_BY_SUBJECT_URL = "/teachers/subject/list/?id=1";
    private static final String TEACHER_URL = "/teachers/1";
    private static final String SAVE_URL = "/teachers/save";
    private static final String EDIT_URL = "/teachers/1/edit";
    private static final String DELETE_URL = "/teachers/1/delete";

    private static final String ALL_VIEW = "teachers/all";
    private static final String BY_ID_VIEW = "teachers/by-id";
    private static final String SHOW_VIEW = "teachers/show";
    private static final String EDIT_VIEW = "teachers/edit";
    private static final String REDIRECT = "redirect:/teachers";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String TEACHER_ATTRIBUTE = "teacher";
    private static final String TEACHERS_ATTRIBUTE = "teachers";
    private static final String DEPARTMENTS_ATTRIBUTE = "departments";
    private static final String DEPARTMENT_ATTRIBUTE = "department";
    private static final String SUBJECTS_ATTRIBUTE = "subjects";
    private static final String SUBJECT_ATTRIBUTE = "subject";
    private static final String EMPLOYEES_ATTRIBUTE = "employees";
    private static final String LECTURES_ATTRIBUTE = "lectures";

    private static final int ACTUAL_ID = 1;

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private LectureService lectureService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private TeachersController teachersController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(teachersController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindTeachers_thenAllView() throws Exception {
        when(teacherService.findAll()).thenReturn(setExpectedTeachers());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(employeeService.findNotTeachers()).thenReturn(setExpectedEmployees());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENT_ATTRIBUTE, new Department()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEES_ATTRIBUTE, setExpectedEmployees()));
    }

    @Test
    void whenFindTeachers_thenServiceException() throws Exception {
        when(teacherService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindTeachers_thenServerError() throws Exception {
        when(teacherService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindTeachersByDepartmentId_thenByDepartmentIdView() throws Exception {
        when(teacherService.findByDepartment(ACTUAL_ID)).thenReturn(setExpectedTeachers());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(employeeService.findNotTeachers()).thenReturn(setExpectedEmployees());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENT_ATTRIBUTE, new Department()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEES_ATTRIBUTE, setExpectedEmployees()));
    }

    @Test
    void whenFindTeachersByDepartmentId_thenServiceException() throws Exception {
        when(teacherService.findByDepartment(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindTeachersByDepartmentId_thenServerError() throws Exception {
        when(teacherService.findByDepartment(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindTeachersBySubjectId_thenBySubjectIdView() throws Exception {
        when(teacherService.findBySubject(ACTUAL_ID)).thenReturn(setExpectedTeachers());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());
        when(employeeService.findNotTeachers()).thenReturn(setExpectedEmployees());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, new Teacher()))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHERS_ATTRIBUTE, setExpectedTeachers()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENT_ATTRIBUTE, new Department()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEES_ATTRIBUTE, setExpectedEmployees()));
    }

    @Test
    void whenFindTeachersBySubjectId_thenServiceException() throws Exception {
        when(teacherService.findBySubject(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindTeachersBySubjectId_thenServerError() throws Exception {
        when(teacherService.findBySubject(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHERS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindTeacher_thenShowView() throws Exception {
        when(teacherService.find(setExpectedTeachers().get(0).getId()))
            .thenReturn(setExpectedTeachers().get(0));
        when(subjectService.findByTeacher(setExpectedTeachers().get(0).getId()))
            .thenReturn(setExpectedSubjects());
        when(lectureService.findByTeacher(setExpectedTeachers().get(0).getId()))
            .thenReturn(setExpectedLectures());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(SHOW_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, setExpectedTeachers().get(0)))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()));
    }

    @Test
    void whenFindTeacher_thenServiceException() throws Exception {
        when(teacherService.find(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindTeacher_thenServerError() throws Exception {
        when(teacherService.find(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewTeacher_thenNewTeacherInRedirectView() throws Exception {
        ArgumentCaptor<Teacher> teacherCapture = ArgumentCaptor.forClass(Teacher.class);
        doNothing().when(teacherService).add(teacherCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/teachers"));
    }

    @Test
    void whenSaveNewTeacher_thenServiceException() throws Exception {
        ArgumentCaptor<Teacher> teacherCapture = ArgumentCaptor.forClass(Teacher.class);
        doThrow(new ServiceException()).when(teacherService).add(teacherCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post("/teachers/save"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewTeacher_thenServerError() throws Exception {
        ArgumentCaptor<Teacher> teacherCapture = ArgumentCaptor.forClass(Teacher.class);
        doThrow(new RuntimeException()).when(teacherService).add(teacherCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindTeacherToUpdate_thenEditView() throws Exception {
        when(teacherService.find(setExpectedTeachers().get(0).getId()))
            .thenReturn(setExpectedTeachers().get(0));
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(EDIT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(TEACHER_ATTRIBUTE, setExpectedTeachers().get(0)))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()));
    }

    @Test
    void whenFindTeacherToUpdate_thenServiceException() throws Exception {
        when(teacherService.find(ACTUAL_ID))
            .thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindTeacherToUpdate_thenServerError() throws Exception {
        when(teacherService.find(ACTUAL_ID))
            .thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateTeacher_thenUpdatedTeacherRedirectView() throws Exception {
        doNothing().when(teacherService).update(ACTUAL_ID, setExpectedTeachers().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(TEACHER_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(TEACHER_ATTRIBUTE));
    }

    @Test
    void whenUpdateTeacher_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(teacherService).update(ACTUAL_ID, setExpectedTeachers().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateTeacher_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(teacherService).update(ACTUAL_ID, setExpectedTeachers().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(TEACHER_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteTeacher_thenNoTeacherInRedirectView() throws Exception {
        doNothing().when(teacherService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));
    }

    @Test
    void whenDeleteTeacher_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(teacherService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteTeacher_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(teacherService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
