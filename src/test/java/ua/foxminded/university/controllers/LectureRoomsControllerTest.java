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
import ua.foxminded.university.domain.LectureRoom;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.LectureRoomService;
import ua.foxminded.university.service.interfaces.LectureService;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedLectures;
import static ua.foxminded.university.data.EntityData.setExpectedRooms;

@ExtendWith(MockitoExtension.class)
class LectureRoomsControllerTest {

    private static final String ROOMS_URL = "/rooms";
    private static final String SAVE_URL = "/rooms/save";
    private static final String EDIT_URL = "/rooms/101/edit";
    private static final String DELETE_URL = "/rooms/101/delete";

    private static final String ALL_VIEW = "rooms/all";
    private static final String REDIRECT = "redirect:/rooms";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String ROOM_ATTRIBUTE = "newRoom";
    private static final String ROOMS_ATTRIBUTE = "rooms";
    private static final String LECTURES_ATTRIBUTE = "lectures";

    private static final int ACTUAL_ID = 101;

    private MockMvc mockMvc;

    @Mock
    private LectureRoomService roomService;

    @Mock
    private LectureService lectureService;

    @InjectMocks
    private LectureRoomsController roomsController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomsController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindLectureRooms_thenAllView() throws Exception {
        when(roomService.findAll()).thenReturn(setExpectedRooms());
        when(lectureService.findAll()).thenReturn(setExpectedLectures());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(ROOMS_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOM_ATTRIBUTE, new LectureRoom()))
            .andExpect(MockMvcResultMatchers.model().attribute(ROOMS_ATTRIBUTE, setExpectedRooms()))
            .andExpect(MockMvcResultMatchers.model().attribute(LECTURES_ATTRIBUTE, setExpectedLectures()));
    }

    @Test
    void whenFindLectureRooms_thenServiceException() throws Exception {
        when(roomService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(ROOMS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindLectureRooms_thenServerError() throws Exception {
        when(roomService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(ROOMS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewLectureRoom_thenNewLectureRoomInRedirectView() throws Exception {
        ArgumentCaptor<LectureRoom> roomCapture = ArgumentCaptor.forClass(LectureRoom.class);
        doNothing().when(roomService).add(roomCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(ROOM_ATTRIBUTE));
    }

    @Test
    void whenSaveNewLectureRoom_thenServiceException() throws Exception {
        ArgumentCaptor<LectureRoom> roomCapture = ArgumentCaptor.forClass(LectureRoom.class);
        doThrow(new ServiceException()).when(roomService).add(roomCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewLectureRoom_thenServerError() throws Exception {
        ArgumentCaptor<LectureRoom> roomCapture = ArgumentCaptor.forClass(LectureRoom.class);
        doThrow(new RuntimeException()).when(roomService).add(roomCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateLectureRoom_thenUpdatedLectureRoomRedirectView() throws Exception {
        doNothing().when(roomService).update(ACTUAL_ID, new LectureRoom());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(ROOM_ATTRIBUTE));
    }

    @Test
    void whenUpdateLectureRoom_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(roomService).update(ACTUAL_ID, new LectureRoom());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateLectureRoom_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(roomService).update(ACTUAL_ID, setExpectedRooms().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteLectureRoom_thenNoLectureRoomInRedirectView() throws Exception {
        doNothing().when(roomService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));
    }

    @Test
    void whenDeleteLectureRoom_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(roomService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteLectureRoom_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(roomService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
