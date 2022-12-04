package ua.foxminded.university.rest_controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.foxminded.university.data.restcontrollers.MarkTestingData;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;
import ua.foxminded.university.util.exceptions.ValidationException;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = {"classpath:2.data.sql"})
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@WithUserDetails("admin@mail.com")
class MarksRestControllerTest extends MarkTestingData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAllMarks_thenMarksList() throws Exception {
        mockMvc.perform(get(SHOW_ALL_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_SIZE)));
    }

    @Test
    void givenSubjectId_whenGetMarksBySubject_thenMarksList() throws Exception {
        mockMvc.perform(get(SHOW_BY_SUBJECT_URL).param("id", String.valueOf(PARAM_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_BY_SUBJECT_SIZE)));
    }

    @Test
    void givenSubjectId_whenGetMarksBySubject_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_BY_SUBJECT_URL).param("id", String.valueOf(NOT_FOUND_ID)))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(LIST_NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidData_whenGetMarksBySubject_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_BY_SUBJECT_URL).param("id", INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenStudentId_whenGetMarksByStudent_thenMarksList() throws Exception {
        mockMvc.perform(get(SHOW_BY_STUDENT_URL).param("id", String.valueOf(PARAM_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_BY_STUDENT_SIZE)));
    }

    @Test
    void givenStudentId_whenGetMarksByStudent_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_BY_STUDENT_URL).param("id", String.valueOf(NOT_FOUND_ID)))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(LIST_NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidData_whenGetMarksByStudent_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_BY_STUDENT_URL).param("id", INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenMark_whenCreateMark_thenNewMark() throws Exception {
        mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateMarkForCreate())))
            .andExpect(status().isOk());
    }

    @Test
    void givenMark_whenCreateMark_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidMark())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenMark_whenCreateMark_thenEntityExistsException() throws Exception {
        mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateExistedMark())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
            .andExpect(result -> assertEquals(EXISTS_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenMarkId_whenGetMarkToUpdate_thenReturnMark() throws Exception {
        mockMvc.perform(get(EDIT_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.value", is(generateMarkForUpdate().getValue().toString())))
            .andExpect(jsonPath("$.student.lastName", is(generateMarkForUpdate().getStudent().getLastName())))
            .andExpect(jsonPath("$.subject.name", is(generateMarkForUpdate().getSubject().getName())));
    }

    @Test
    void givenMarkId_whenGetMarkToUpdate_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(EDIT_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenMarkId_whenGetMarkToUpdate_thenServerError() throws Exception {
        mockMvc.perform(get(EDIT_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenMark_whenUpdateMark_thenUpdatedMark() throws Exception {
        mockMvc.perform(put(UPDATE_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedMark())))
            .andExpect(status().isOk());
    }

    @Test
    void givenMark_whenUpdateMark_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(put(UPDATE_URL, NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedMark())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenMark_whenUpdateMark_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(UPDATE_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidMark())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenMark_whenUpdateMark_thenEntityExistsException() throws Exception {
        mockMvc.perform(put(UPDATE_URL, ID_TO_UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedMark())))
            .andExpect(status().is4xxClientError())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
            .andExpect(result -> assertEquals(EXISTS_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenMark_whenUpdateMark_thenServerError() throws Exception {
        mockMvc.perform(put(UPDATE_URL, INVALID_DATA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedMark())))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenMarkId_whenDeleteMark_thenNoMark() throws Exception {
        mockMvc.perform(delete(DELETE_URL, ID))
            .andExpect(status().isOk());
    }

    @Test
    void givenMarkId_whenDeleteMark_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(delete(DELETE_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenMarkId_whenDeleteMark_thenServerError() throws Exception {
        mockMvc.perform(delete(DELETE_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }
}
