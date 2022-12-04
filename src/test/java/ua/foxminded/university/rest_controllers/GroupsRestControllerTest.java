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
import ua.foxminded.university.data.restcontrollers.GroupTestingData;
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
class GroupsRestControllerTest extends GroupTestingData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAllGroups_thenGroupsList() throws Exception {
        mockMvc.perform(get(SHOW_ALL_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_SIZE)));
    }

    @Test
    void givenDepartmentId_whenGetGroupsByDepartment_thenGroupsList() throws Exception {
        mockMvc.perform(get(SHOW_BY_DEPARTMENT_URL).param("id", String.valueOf(PARAM_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_BY_DEPARTMENT_SIZE)));
    }

    @Test
    void givenDepartmentId_whenGetGroupsByDepartment_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_BY_DEPARTMENT_URL).param("id", String.valueOf(NOT_FOUND_ID)))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(LIST_NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidData_whenGetGroupsByDepartment_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_BY_DEPARTMENT_URL).param("id", INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenDepartmentId_whenGetGroupsBySubject_thenGroupsList() throws Exception {
        mockMvc.perform(get(SHOW_BY_SUBJECT_URL).param("id", String.valueOf(PARAM_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_BY_SUBJECT_SIZE)));
    }

    @Test
    void givenDepartmentId_whenGetGroupsBySubject_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_BY_SUBJECT_URL).param("id", String.valueOf(NOT_FOUND_ID)))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(LIST_NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidData_whenGetGroupsBySubject_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_BY_SUBJECT_URL).param("id", INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenDepartmentId_whenGetGroupsByLecture_thenGroupsList() throws Exception {
        mockMvc.perform(get(SHOW_BY_LECTURE_URL).param("id", String.valueOf(PARAM_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_BY_LECTURE_SIZE)));
    }

    @Test
    void givenDepartmentId_whenGetGroupsByLecture_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_BY_LECTURE_URL).param("id", String.valueOf(NOT_FOUND_ID)))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(LIST_NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidData_whenGetGroupsByLecture_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_BY_LECTURE_URL).param("id", INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenGroupId_whenGetGroupById_thenReturnGroup() throws Exception {
        mockMvc.perform(get(SHOW_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(generateGroupForFindById().getName())))
            .andExpect(jsonPath("$.departmentName", is(generateGroupForFindById().getDepartmentName())));
    }

    @Test
    void givenGroupId_whenGetGroupById_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidGroupId_whenGetGroupById_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenGroup_whenCreateGroup_thenNewGroup() throws Exception {
        mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateGroupForCreate())))
            .andExpect(status().isOk());
    }

    @Test
    void givenGroup_whenCreateGroup_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidGroup())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenGroup_whenCreateGroup_thenEntityExistsException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateExistedGroup())))
            .andExpect(status().is4xxClientError())
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(EXISTS_ERROR_RESPONSE, content);
    }

    @Test
    void givenGroupId_whenGetGroupToUpdate_thenReturnGroup() throws Exception {
        mockMvc.perform(get(EDIT_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(generateGroupForUpdate().getId())))
            .andExpect(jsonPath("$.name", is(generateGroupForUpdate().getName())))
            .andExpect(jsonPath("$.departmentId", is(generateGroupForUpdate().getDepartmentId())))
            .andExpect(jsonPath("$.departmentName", is(generateGroupForUpdate().getDepartmentName())));
    }

    @Test
    void givenGroupId_whenGetGroupToUpdate_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(EDIT_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenGroupId_whenGetGroupToUpdate_thenServerError() throws Exception {
        mockMvc.perform(get(EDIT_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenGroup_whenUpdateGroup_thenUpdatedGroup() throws Exception {
        mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedGroup())))
            .andExpect(status().isOk());
    }

    @Test
    void givenGroup_whenUpdateGroup_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(put(SHOW_URL, NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedGroup())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenGroup_whenUpdateGroup_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidGroup())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenGroup_whenUpdateGroup_thenEntityExistsException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SHOW_URL, ID_TO_UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedGroup())))
            .andExpect(status().is4xxClientError())
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(EXISTS_ERROR_RESPONSE, content);
    }

    @Test
    void givenGroup_whenUpdateGroup_thenServerError() throws Exception {
        mockMvc.perform(put(SHOW_URL, INVALID_DATA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedGroup())))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenGroupId_whenDeleteGroup_thenNoGroup() throws Exception {
        mockMvc.perform(delete(DELETE_URL, ID))
            .andExpect(status().isOk());
    }

    @Test
    void givenGroupId_whenDeleteGroup_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(delete(DELETE_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenGroupId_whenDeleteGroup_thenServerError() throws Exception {
        mockMvc.perform(delete(DELETE_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }
}
