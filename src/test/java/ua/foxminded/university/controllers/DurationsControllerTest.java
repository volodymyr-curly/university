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
import ua.foxminded.university.domain.Duration;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.DurationService;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedDurations;

@ExtendWith(MockitoExtension.class)
class DurationsControllerTest {

    private static final String DURATIONS_URL = "/durations";
    private static final String SAVE_URL = "/durations/save";
    private static final String EDIT_URL = "/durations/1";
    private static final String DELETE_URL = "/durations/1/delete";

    private static final String ALL_VIEW = "durations/all";
    private static final String REDIRECT = "redirect:/durations";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String DURATION_ATTRIBUTE = "duration";
    private static final String DURATIONS_ATTRIBUTE = "durations";

    private static final int ACTUAL_ID = 1;

    private MockMvc mockMvc;

    @Mock
    private DurationService durationService;

    @InjectMocks
    private DurationsController durationsController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(durationsController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindDurations_thenAllView() throws Exception {
        when(durationService.findAll()).thenReturn(setExpectedDurations());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DURATIONS_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATION_ATTRIBUTE, new Duration()))
            .andExpect(MockMvcResultMatchers.model().attribute(DURATIONS_ATTRIBUTE, setExpectedDurations()));
    }

    @Test
    void whenFindDurations_thenServiceException() throws Exception {
        when(durationService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DURATIONS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindDurations_thenServerError() throws Exception {
        when(durationService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DURATIONS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewDuration_thenNewDurationInRedirectView() throws Exception {
        ArgumentCaptor<Duration> durationCapture = ArgumentCaptor.forClass(Duration.class);
        doNothing().when(durationService).add(durationCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(DURATION_ATTRIBUTE));
    }

    @Test
    void whenSaveNewDuration_thenServiceException() throws Exception {
        ArgumentCaptor<Duration> durationCapture = ArgumentCaptor.forClass(Duration.class);
        doThrow(new ServiceException()).when(durationService).add(durationCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewDuration_thenServerError() throws Exception {
        ArgumentCaptor<Duration> durationCapture = ArgumentCaptor.forClass(Duration.class);
        doThrow(new RuntimeException()).when(durationService).add(durationCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateDuration_thenUpdatedDurationRedirectView() throws Exception {
        doNothing().when(durationService).update(ACTUAL_ID, setExpectedDurations().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(DURATION_ATTRIBUTE));
    }

    @Test
    void whenUpdateDuration_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(durationService).update(ACTUAL_ID, setExpectedDurations().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateDuration_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(durationService).update(ACTUAL_ID, setExpectedDurations().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteDuration_thenNoDurationInRedirectView() throws Exception {
        doNothing().when(durationService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));
    }

    @Test
    void whenDeleteDuration_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(durationService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteDuration_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(durationService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
