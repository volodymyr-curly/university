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
import ua.foxminded.university.data.restcontrollers.StudentTestingData;
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
class StudentsRestControllerTest extends StudentTestingData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAllStudents_thenStudentsList() throws Exception {
        mockMvc.perform(get(SHOW_ALL_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_SIZE)));
    }

    @Test
    void givenGroupId_whenGetStudentsByGroup_thenStudentsList() throws Exception {
        mockMvc.perform(get(SHOW_BY_GROUP_URL).param("id", String.valueOf(PARAM_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_BY_GROUP_SIZE)));
    }

    @Test
    void givenGroupId_whenGetStudentsByGroup_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_BY_GROUP_URL).param("id", String.valueOf(NOT_FOUND_ID)))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(LIST_NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidData_whenGetStudentsByGroup_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_BY_GROUP_URL).param("id", INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenStudentId_whenGetStudentById_thenReturnStudent() throws Exception {
        mockMvc.perform(get(SHOW_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName", is(generateStudentForFindById().getLastName())))
            .andExpect(jsonPath("$.firstName", is(generateStudentForFindById().getFirstName())))
            .andExpect(jsonPath("$.gender", is(generateStudentForFindById().getGender().toString())))
            .andExpect(jsonPath("$.groupName", is(generateStudentForFindById().getGroupName())));
    }

    @Test
    void givenStudentId_whenGetStudentById_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidStudentId_whenGetStudentById_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenStudent_whenCreateStudent_thenNewStudent() throws Exception {
        mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateStudentForCreate())))
            .andExpect(status().isOk());
    }

    @Test
    void givenNotUniqueStudentsEmail_whenCreateStudent_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateStudentWithNotUniqueEmail())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(EMAIL_VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenStudent_whenCreateStudent_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidStudent())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenStudent_whenCreateStudent_thenEntityExistsException() throws Exception {
        mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateExistedStudent())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
            .andExpect(result -> assertEquals(EXISTS_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenStudentId_whenGetStudentToUpdate_thenReturnStudent() throws Exception {
        mockMvc.perform(get(EDIT_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName", is(generateStudentForUpdate().getLastName())))
            .andExpect(jsonPath("$.firstName", is(generateStudentForUpdate().getFirstName())))
            .andExpect(jsonPath("$.gender", is(generateStudentForUpdate().getGender().toString())))
            .andExpect(jsonPath("$.groupName", is(generateStudentForUpdate().getGroupName())));
    }

    @Test
    void givenStudentId_whenGetStudentToUpdate_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(EDIT_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenStudentId_whenGetStudentToUpdate_thenServerError() throws Exception {
        mockMvc.perform(get(EDIT_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenStudent_whenUpdateStudent_thenUpdatedStudent() throws Exception {
        mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedStudent())))
            .andExpect(status().isOk());
    }

    @Test
    void givenStudent_whenUpdateStudent_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(put(SHOW_URL, NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedStudent())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenNotUniqueStudentsEmail_whenUpdateStudent_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedStudentNotWithUniqueEmail())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(EMAIL_VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenStudent_whenUpdateStudent_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidStudent())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenStudent_whenUpdateStudent_thenEntityExistsException() throws Exception {
        mockMvc.perform(put(SHOW_URL, ID_TO_UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedStudent())))
            .andExpect(status().is4xxClientError())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
            .andExpect(result -> assertEquals(EXISTS_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenStudent_whenUpdateStudent_thenServerError() throws Exception {
        mockMvc.perform(put(SHOW_URL, INVALID_DATA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedStudent())))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenStudentId_whenDeleteStudent_thenNoStudent() throws Exception {
        mockMvc.perform(delete(DELETE_URL, ID))
            .andExpect(status().isOk());
    }

    @Test
    void givenStudentId_whenDeleteStudent_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(delete(DELETE_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenStudentId_whenDeleteStudent_thenServerError() throws Exception {
        mockMvc.perform(delete(DELETE_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }
}
