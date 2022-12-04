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
import ua.foxminded.university.domain.Mark;
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.MarkService;
import ua.foxminded.university.service.interfaces.StudentService;
import ua.foxminded.university.service.interfaces.SubjectService;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(MockitoExtension.class)
class MarksControllerTest {

    private static final String MARKS_URL = "/marks";
    private static final String MARKS_BY_STUDENT_URL = "/marks/student/list/?id=1";
    private static final String MARKS_BY_SUBJECT_URL = "/marks/subject/list/?id=1";
    private static final String SAVE_URL = "/marks/save";
    private static final String EDIT_URL = "/marks/1/edit";
    private static final String DELETE_URL = "/marks/1/delete";

    private static final String ALL_VIEW = "marks/all";
    private static final String BY_ID_VIEW = "marks/by-id";
    private static final String REDIRECT = "redirect:/marks";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String MARKS_ATTRIBUTE = "marks";
    private static final String MARK_ATTRIBUTE = "newMark";
    private static final String STUDENTS_ATTRIBUTE = "students";
    private static final String STUDENT_ATTRIBUTE = "student";
    private static final String SUBJECTS_ATTRIBUTE = "subjects";
    private static final String SUBJECT_ATTRIBUTE = "subject";

    private static final int ACTUAL_ID = 1;

    private MockMvc mockMvc;

    @Mock
    private MarkService markService;

    @Mock
    private StudentService studentService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private MarksController marksController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(marksController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindMarks_thenAllView() throws Exception {
        when(markService.findAll()).thenReturn(setExpectedMarks());
        when(studentService.findAll()).thenReturn(setExpectedStudents());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(MARK_ATTRIBUTE, new Mark()))
            .andExpect(MockMvcResultMatchers.model().attribute(MARKS_ATTRIBUTE, setExpectedMarks()))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENT_ATTRIBUTE, new Student()))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENTS_ATTRIBUTE, setExpectedStudents()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()));
    }

    @Test
    void whenFindMarks_thenServiceException() throws Exception {
        when(markService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindMarks_thenServerError() throws Exception {
        when(markService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindMarksByStudentId_thenByStudentIdView() throws Exception {
        when(markService.findByStudent(ACTUAL_ID)).thenReturn(setExpectedMarks());
        when(studentService.findAll()).thenReturn(setExpectedStudents());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_BY_STUDENT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(MARK_ATTRIBUTE, new Mark()))
            .andExpect(MockMvcResultMatchers.model().attribute(MARKS_ATTRIBUTE, setExpectedMarks()))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENT_ATTRIBUTE, new Student()))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENTS_ATTRIBUTE, setExpectedStudents()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()));
    }

    @Test
    void whenFindMarksByStudentId_thenServiceException() throws Exception {
        when(markService.findByStudent(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_BY_STUDENT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindMarksByStudentId_thenServerError() throws Exception {
        when(markService.findByStudent(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_BY_STUDENT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindMarksBySubjectId_thenBySubjectIdView() throws Exception {
        when(markService.findBySubject(ACTUAL_ID)).thenReturn(setExpectedMarks());
        when(studentService.findAll()).thenReturn(setExpectedStudents());
        when(subjectService.findAll()).thenReturn(setExpectedSubjects());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(MARK_ATTRIBUTE, new Mark()))
            .andExpect(MockMvcResultMatchers.model().attribute(MARKS_ATTRIBUTE, setExpectedMarks()))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENT_ATTRIBUTE, new Student()))
            .andExpect(MockMvcResultMatchers.model().attribute(STUDENTS_ATTRIBUTE, setExpectedStudents()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECT_ATTRIBUTE, new Subject()))
            .andExpect(MockMvcResultMatchers.model().attribute(SUBJECTS_ATTRIBUTE, setExpectedSubjects()));
    }

    @Test
    void whenFindMarksBySubjectId_thenServiceException() throws Exception {
        when(markService.findBySubject(ACTUAL_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindMarksBySubjectId_thenServerError() throws Exception {
        when(markService.findBySubject(ACTUAL_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(MARKS_BY_SUBJECT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewMark_thenNewMarkInRedirectView() throws Exception {
        ArgumentCaptor<Mark> markCapture = ArgumentCaptor.forClass(Mark.class);
        doNothing().when(markService).add(markCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/marks"));
    }

    @Test
    void whenSaveNewMark_thenServiceException() throws Exception {
        ArgumentCaptor<Mark> markCapture = ArgumentCaptor.forClass(Mark.class);
        doThrow(new ServiceException()).when(markService).add(markCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post("/marks/save"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewMark_thenServerError() throws Exception {
        ArgumentCaptor<Mark> markCapture = ArgumentCaptor.forClass(Mark.class);
        doThrow(new RuntimeException()).when(markService).add(markCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateMark_thenUpdatedMarkRedirectView() throws Exception {
        doNothing().when(markService).update(ACTUAL_ID, setExpectedMarks().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(MARK_ATTRIBUTE));
    }

    @Test
    void whenUpdateMark_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(markService).update(ACTUAL_ID, setExpectedMarks().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateMark_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(markService).update(ACTUAL_ID, setExpectedMarks().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteMark_thenNoMarkInRedirectView() throws Exception {
        doNothing().when(markService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));
    }

    @Test
    void whenDeleteMark_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(markService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteMark_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(markService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
