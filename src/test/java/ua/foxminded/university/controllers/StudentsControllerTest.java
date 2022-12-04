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
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.GroupService;
import ua.foxminded.university.service.interfaces.MarkService;
import ua.foxminded.university.service.interfaces.StudentService;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {

    private static final String STUDENTS_URL = "/students";
    private static final String STUDENTS_BY_GROUP_URL = "/students/group/list/?id=1";
    private static final String STUDENT_URL = "/students/1";
    private static final String SAVE_URL = "/students/save";
    private static final String EDIT_URL = "/students/1/edit";
    private static final String DELETE_URL = "/students/1/delete";

    private static final String ALL_VIEW = "students/all";
    private static final String BY_GROUP_ID_VIEW = "students/by-id";
    private static final String SHOW_VIEW = "students/show";
    private static final String EDIT_VIEW = "students/edit";
    private static final String REDIRECT = "redirect:/students";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String STUDENTS_ATTRIBUTE = "students";
    private static final String STUDENT_ATTRIBUTE = "student";
    private static final String GROUPS_ATTRIBUTE = "groups";
    private static final String GROUP_ATTRIBUTE = "group";
    private static final String MARKS_ATTRIBUTE = "marks";

    private static final int ACTUAL_ID = 1;

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @Mock
    private GroupService groupService;

    @Mock
    private MarkService markService;

    @InjectMocks
    private StudentsController studentsController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentsController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindStudents_thenAllView() throws Exception {
        when(studentService.findAll()).thenReturn(setExpectedStudents());
        when(groupService.findAll()).thenReturn(setExpectedGroups());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENTS_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENT_ATTRIBUTE, new Student()))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENTS_ATTRIBUTE, setExpectedStudents()))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()))
            .andExpect(MockMvcResultMatchers.model().attributeExists(GROUP_ATTRIBUTE));
    }

    @Test
    void whenFindStudents_thenServiceException() throws Exception {
        when(studentService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENTS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindStudents_thenServerError() throws Exception {
        when(studentService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENTS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindStudentsByGroupId_thenByGroupIdView() throws Exception {
        when(studentService.findByGroup(ACTUAL_ID)).thenReturn(setExpectedStudents());
        when(groupService.findAll()).thenReturn(setExpectedGroups());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENTS_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_GROUP_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENT_ATTRIBUTE, new Student()))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENTS_ATTRIBUTE, setExpectedStudents()))
            .andExpect(MockMvcResultMatchers.model().attribute(GROUPS_ATTRIBUTE, setExpectedGroups()));
    }

    @Test
    void whenFindStudentsByGroupId_thenServiceException() throws Exception {
        when(studentService.findByGroup(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENTS_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindStudentsByGroupId_thenServerError() throws Exception {
        when(studentService.findByGroup(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENTS_BY_GROUP_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindStudent_thenShowView() throws Exception {
        when(studentService.find(setExpectedStudents().get(0).getId()))
            .thenReturn(setExpectedStudents().get(0));
        when(markService.findByStudent(setExpectedStudents().get(0).getId()))
            .thenReturn(setExpectedMarks());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(SHOW_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENT_ATTRIBUTE, setExpectedStudents().get(0)))
            .andExpect(MockMvcResultMatchers.model().attribute(MARKS_ATTRIBUTE, setExpectedMarks()));
    }

    @Test
    void whenFindStudent_thenServiceException() throws Exception {
        when(studentService.find(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindStudent_thenServerError() throws Exception {
        when(studentService.find(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(STUDENT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewStudent_thenNewStudentInRedirectView() throws Exception {
        ArgumentCaptor<Student> studentCapture = ArgumentCaptor.forClass(Student.class);
        doNothing().when(studentService).add(studentCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/students"));
    }

    @Test
    void whenSaveNewStudent_thenServiceException() throws Exception {
        ArgumentCaptor<Student> studentCapture = ArgumentCaptor.forClass(Student.class);
        doThrow(new ServiceException()).when(studentService).add(studentCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewStudent_thenServerError() throws Exception {
        ArgumentCaptor<Student> studentCapture = ArgumentCaptor.forClass(Student.class);
        doThrow(new RuntimeException()).when(studentService).add(studentCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindStudentToUpdate_thenEditView() throws Exception {
        when(studentService.find(setExpectedStudents().get(0).getId()))
            .thenReturn(setExpectedStudents().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(EDIT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENT_ATTRIBUTE, setExpectedStudents().get(0)));
    }

    @Test
    void whenFindStudentToUpdate_thenServiceException() throws Exception {
        when(studentService.find(ACTUAL_ID))
            .thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindStudentToUpdate_thenServerError() throws Exception {
        when(studentService.find(ACTUAL_ID))
            .thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateStudent_thenUpdatedStudentRedirectView() throws Exception {
        doNothing().when(studentService).update(ACTUAL_ID, new Student());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(STUDENT_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(STUDENT_ATTRIBUTE));
    }

    @Test
    void whenUpdateStudent_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(studentService).update(ACTUAL_ID, new Student());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(STUDENT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateStudent_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(studentService).update(ACTUAL_ID, setExpectedStudents().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(STUDENT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteStudent_thenNoStudentInRedirectView() throws Exception {
        doNothing().when(studentService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));

    }

    @Test
    void whenDeleteStudent_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(studentService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteStudent_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(studentService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
