package ua.foxminded.university.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UniversityControllerTest {

    private static final String UNIVERSITY_URL = "/";
    private static final String INDEX_VIEW = "index";

    private MockMvc mockMvc;
    private UniversityController universityController;

    @BeforeEach
    void init() {
        universityController = new UniversityController();
        mockMvc = MockMvcBuilders.standaloneSetup(universityController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenOpenUniversityPage_thenIndexView() throws Exception {
        this.mockMvc
            .perform(MockMvcRequestBuilders.get(UNIVERSITY_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(INDEX_VIEW));
    }
}
