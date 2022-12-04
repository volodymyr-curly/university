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
import ua.foxminded.university.data.restcontrollers.RoomTestingData;
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
class RoomsRestControllerTest extends RoomTestingData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAllRooms_thenRoomsList() throws Exception {
        mockMvc.perform(get(SHOW_ALL_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(LIST_SIZE)));
    }

    @Test
    void givenRoomId_whenGetRoomById_thenReturnRoom() throws Exception {
        mockMvc.perform(get(SHOW_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.number", is(generateRoomForFindById().getNumber())))
            .andExpect(jsonPath("$.capacity", is(generateRoomForFindById().getCapacity())));
    }

    @Test
    void givenRoomId_whenGetRoomById_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(SHOW_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenInvalidRoomId_whenGetRoomById_thenServerError() throws Exception {
        mockMvc.perform(get(SHOW_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenRoom_whenCreateRoom_thenNewRoom() throws Exception {
        mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateRoomForCreate())))
            .andExpect(status().isOk());
    }

    @Test
    void givenRoom_whenCreateRoom_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidRoom())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenRoom_whenCreateRoom_thenEntityExistsException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SHOW_ALL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateExistedRoom())))
            .andExpect(status().is4xxClientError())
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(EXISTS_ERROR_RESPONSE, content);
    }

    @Test
    void givenRoomId_whenGetRoomToUpdate_thenReturnRoom() throws Exception {
        mockMvc.perform(get(EDIT_URL, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.number", is(generateRoomForUpdate().getNumber())))
            .andExpect(jsonPath("$.capacity", is(generateRoomForUpdate().getCapacity())));
    }

    @Test
    void givenRoomId_whenGetRoomToUpdate_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(get(EDIT_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenRoomId_whenGetRoomToUpdate_thenServerError() throws Exception {
        mockMvc.perform(get(EDIT_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenRoom_whenUpdateRoom_thenUpdatedRoom() throws Exception {
        mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedRoom())))
            .andExpect(status().isOk());
    }

    @Test
    void givenRoom_whenUpdateRoom_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(put(SHOW_URL, NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedRoom())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenRoom_whenUpdateRoom_thenValidationException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SHOW_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidRoom())))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(VALIDATION_ERROR_RESPONSE, content);
    }

    @Test
    void givenRoom_whenUpdateRoom_thenEntityExistsException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SHOW_URL, ID_TO_UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateExistedRoom())))
            .andExpect(status().is4xxClientError())
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(EXISTS_ERROR_RESPONSE, content);
    }

    @Test
    void givenRoom_whenUpdateRoom_thenServerError() throws Exception {
        mockMvc.perform(put(SHOW_URL, INVALID_DATA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdatedRoom())))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void givenRoomId_whenDeleteRoom_thenNoRoom() throws Exception {
        mockMvc.perform(delete(DELETE_URL, ID))
            .andExpect(status().isOk());
    }

    @Test
    void givenRoomId_whenDeleteRoom_thenEntityNotFoundException() throws Exception {
        mockMvc.perform(delete(DELETE_URL, NOT_FOUND_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(NOT_FOUND_MESSAGE,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenRoomId_whenDeleteRoom_thenServerError() throws Exception {
        mockMvc.perform(delete(DELETE_URL, INVALID_DATA))
            .andExpect(status().is5xxServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }
}
