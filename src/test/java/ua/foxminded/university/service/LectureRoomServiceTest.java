package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.LectureRoom;
import ua.foxminded.university.repository.LectureRoomRepository;
import ua.foxminded.university.service.interfaces.LectureRoomService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedRooms;
import static ua.foxminded.university.data.EntityData.setInitialRooms;

@ExtendWith(SpringExtension.class)
@Import(LectureRoomServiceImpl.class)
class LectureRoomServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final LectureRoom EXPECTED_ROOM = setExpectedRooms().get(0);

    @Autowired
    private LectureRoomService roomService;

    @MockBean
    private LectureRoomRepository roomRepository;

    @Test
    void givenLectureRoom_whenAddLectureRoom_thenLectureRoom() {
        when(roomRepository.save(any(LectureRoom.class))).thenReturn(EXPECTED_ROOM);

        roomService.add(EXPECTED_ROOM);

        verify(roomRepository, times(INVOCATION_NUMBER)).save(any(LectureRoom.class));
    }

    @Test
    void givenLectureRoom_whenUpdateLectureRoom_thenUpdatedLectureRoom() {
        when(roomRepository.save(any(LectureRoom.class))).thenReturn(EXPECTED_ROOM);
        when(roomRepository.existsById(ACTUAL_ID)).thenReturn(true);

        roomService.update(ACTUAL_ID, EXPECTED_ROOM);

        verify(roomRepository, times(INVOCATION_NUMBER)).save(any(LectureRoom.class));
    }

    @Test
    void givenLectureRoom_whenDeleteLectureRoom_thenNoLectureRoom() {
        doNothing().when(roomRepository).deleteById(ACTUAL_ID);
        when(roomRepository.existsById(ACTUAL_ID)).thenReturn(true);

        roomService.delete(ACTUAL_ID);

        verify(roomRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenLectureRoomId_whenFindLectureRoom_thenLectureRoom() {
        when(roomRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_ROOM));

        LectureRoom actualRoom = roomService.find(ACTUAL_ID);

        assertEquals(EXPECTED_ROOM, actualRoom);
        verify(roomRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindLectureRooms_thenListOfRooms() {
        when(roomRepository.findAll()).thenReturn(setInitialRooms());

        List<LectureRoom> actualFaculties = roomService.findAll();

        assertEquals(setExpectedRooms(), actualFaculties);
        verify(roomRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenLectureRoom_whenUpdateLectureRoom_thenEntityNotFoundException() {
        when(roomRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomService.update(ACTUAL_ID, EXPECTED_ROOM));
    }

    @Test
    void givenLectureRoom_whenDeleteLectureRoom_thenEntityNotFoundException() {
        when(roomRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roomService.delete(ACTUAL_ID));
    }

    @Test
    void givenLectureRoomId_whenFindLectureRoom_thenEntityNotFoundException() {
        when(roomRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> roomService.find(ACTUAL_ID));
    }

    @Test
    void whenFindLectureRooms_thenEntityNotFoundException() {
        when(roomRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> roomService.findAll());
    }
}
