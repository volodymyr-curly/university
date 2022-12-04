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
import ua.foxminded.university.domain.Faculty;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.FacultyService;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedFaculties;

@ExtendWith(MockitoExtension.class)
class FacultiesControllerTest {

    private static final String FACULTIES_URL = "/faculties";
    private static final String SAVE_URL = "/faculties/save";
    private static final String EDIT_URL = "/faculties/1/edit";
    private static final String DELETE_URL = "/faculties/1/delete";

    private static final String ALL_VIEW = "faculties/all";
    private static final String REDIRECT_VIEW = "redirect:/faculties";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String FACULTIES_ATTRIBUTE = "faculties";
    private static final String NEW_FACULTY_ATTRIBUTE = "newFaculty";

    private static final int ACTUAL_ID = 1;

    private MockMvc mockMvc;

    @Mock
    private FacultyService facultyService;

    @InjectMocks
    private FacultiesController facultiesController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(facultiesController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindFaculties_thenAllView() throws Exception {
        when(facultyService.findAll()).thenReturn(setExpectedFaculties());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(FACULTIES_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(NEW_FACULTY_ATTRIBUTE, new Faculty()))
            .andExpect(MockMvcResultMatchers.model().attribute(FACULTIES_ATTRIBUTE, setExpectedFaculties()));
    }

    @Test
    void whenFindFaculties_thenServiceException() throws Exception {
        when(facultyService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(FACULTIES_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindFaculties_thenServerError() throws Exception {
        when(facultyService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(FACULTIES_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewFaculty_thenNewFacultyInRedirectView() throws Exception {
        ArgumentCaptor<Faculty> facultyCapture = ArgumentCaptor.forClass(Faculty.class);
        doNothing().when(facultyService).add(facultyCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(NEW_FACULTY_ATTRIBUTE));
    }

    @Test
    void whenSaveNewFaculty_thenServiceException() throws Exception {
        ArgumentCaptor<Faculty> facultyCapture = ArgumentCaptor.forClass(Faculty.class);
        doThrow(new ServiceException()).when(facultyService).add(facultyCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewFaculty_thenServerError() throws Exception {
        ArgumentCaptor<Faculty> facultyCapture = ArgumentCaptor.forClass(Faculty.class);
        doThrow(new RuntimeException()).when(facultyService).add(facultyCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateFaculty_thenUpdatedFacultyRedirectView() throws Exception {
        doNothing().when(facultyService).update(ACTUAL_ID, setExpectedFaculties().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(NEW_FACULTY_ATTRIBUTE));
    }

    @Test
    void whenUpdateFaculty_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(facultyService).update(ACTUAL_ID, setExpectedFaculties().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateFaculty_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(facultyService).update(ACTUAL_ID, setExpectedFaculties().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteFaculty_thenNoFacultyInRedirectView() throws Exception {
        doNothing().when(facultyService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT_VIEW));
    }

    @Test
    void whenDeleteFaculty_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(facultyService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteFaculty_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(facultyService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
