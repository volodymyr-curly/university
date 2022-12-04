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
import ua.foxminded.university.data.restcontrollers.EmployeeTestingData;
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
class EmployeesRestControllerTest extends EmployeeTestingData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAllEmployees_thenEmployeesList() throws Exception {
        mockMvc.perform(get(SHOW_ALL_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_SIZE)));
    }

    @Test
    void whenGetNotTeachers_thenEmployeesList() throws Exception {
        mockMvc.perform(get(SHOW_NOT_TEACHERS_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_NOT_TEACHERS_SIZE)));
    }

    @Test
    void givenDepartmentId_whenGetEmployeesByDepartment_thenEmployeesList() throws Exception {
        mockMvc.perform(get(SHOW_BY_DEPARTMENT_URL).param("id", String.valueOf(PARAM_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_BY_DEPARTMENT_SIZE)));
    }

    @Test
    void givenDepartmentId_whenGetEmployeesByDepartment_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_BY_DEPARTMENT_URL).param("id", String.valueOf(NOT_FOUND_ID)))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(LIST_NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidData_whenGetEmployeesByDepartment_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_BY_DEPARTMENT_URL).param("id", INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        mockMvc.perform(get(SHOW_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName", is(generateEmployeeForFindById().getLastName())))
            .andExpect(jsonPath("$.firstName", is(generateEmployeeForFindById().getFirstName())))
            .andExpect(jsonPath("$.gender", is(generateEmployeeForFindById().getGender().toString())))
            .andExpect(jsonPath("$.departmentName", is(generateEmployeeForFindById().getDepartmentName())));
    }

    @Test
    void givenEmployeeId_whenGetEmployeeById_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidEmployeeId_whenGetEmployeeById_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenEmployee_whenCreateEmployee_thenNewEmployee() throws Exception {
        mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateEmployeeForCreate())))
            .andExpect(status().isOk());
    }

    @Test
    void givenNotUniqueEmployeesEmail_whenCreateEmployee_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateEmployeeWithNotUniqueEmail())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(EMAIL_VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenEmployee_whenCreateEmployee_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidEmployee())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenEmployee_whenCreateEmployee_thenEntityExistsException() throws Exception {
        mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateExistedEmployee())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
            .andExpect(result -> assertEquals(EXISTS_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenEmployeeId_whenGetEmployeeToUpdate_thenReturnEmployee() throws Exception {
        mockMvc.perform(get(EDIT_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName", is(generateEmployeeForUpdate().getLastName())))
            .andExpect(jsonPath("$.firstName", is(generateEmployeeForUpdate().getFirstName())))
            .andExpect(jsonPath("$.gender", is(generateEmployeeForUpdate().getGender().toString())))
            .andExpect(jsonPath("$.departmentName", is(generateEmployeeForUpdate().getDepartmentName())));
    }

    @Test
    void givenEmployeeId_whenGetEmployeeToUpdate_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(EDIT_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenEmployeeId_whenGetEmployeeToUpdate_thenServerError() throws Exception {
        mockMvc.perform(get(EDIT_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenUpdatedEmployee() throws Exception {
        mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedEmployee())))
            .andExpect(status().isOk());
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(put(SHOW_URL, NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedEmployee())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenNotUniqueEmployeesEmail_whenUpdateEmployee_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedEmployeeWithNotUniqueEmail())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(EMAIL_VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidEmployee())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenEntityExistsException() throws Exception {
        mockMvc.perform(put(SHOW_URL, ID_TO_UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateExistedEmployee())))
            .andExpect(status().is4xxClientError())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
            .andExpect(result -> assertEquals(EXISTS_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenServerError() throws Exception {
        mockMvc.perform(put(SHOW_URL, INVALID_DATA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedEmployee())))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_thenNoEmployee() throws Exception {
        mockMvc.perform(delete(DELETE_URL, ID))
            .andExpect(status().isOk());
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(delete(DELETE_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_thenServerError() throws Exception {
        mockMvc.perform(delete(DELETE_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }
}
