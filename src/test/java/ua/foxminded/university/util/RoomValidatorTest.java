package ua.foxminded.university.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.LectureRoom;
import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.dto.room.RoomResponseDTO;
import ua.foxminded.university.repository.LectureRoomRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;
import ua.foxminded.university.util.validators.RoomValidator;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.DTOData.*;
import static ua.foxminded.university.data.EntityData.setExpectedRooms;

@ExtendWith(SpringExtension.class)
@Import(RoomValidator.class)
class RoomValidatorTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final int EXPECTED_ERRORS_COUNT = 1;
    private static final String EXIST_MESSAGE = "LectureRoom is already exist";
    private static final LectureRoom EXPECTED_ROOM = setExpectedRooms().get(0);
    private static final LectureRoom EXPECTED_UPDATED_ROOM = setExpectedRooms().get(1);
    private static final RoomRequestDTO ROOM_DTO = setRoomDTO();
    private static final RoomRequestDTO UPDATED_ROOM_DTO = setUpdatedRoomDTO();

    @Autowired
    private Validator roomValidator;

    @MockBean
    private LectureRoomRepository roomRepository;

    @Test
    void whenValidate_thenSupportsClass() {
        assertTrue(roomValidator.supports(RoomRequestDTO.class));
        assertFalse(roomValidator.supports(Object.class));
    }

    @Test
    void givenNewLectureRoom_whenValidate_thenNoError() {
        when(roomRepository.findByNumber(ROOM_DTO.getNumber())).thenReturn(Optional.empty());
        Errors errors = new BeanPropertyBindingResult(new RoomResponseDTO(), "");

        roomValidator.validate(ROOM_DTO, errors);

        verify(roomRepository, times(INVOCATION_NUMBER)).findByNumber(ROOM_DTO.getNumber());
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenNewLectureRoom_whenValidate_thenError() {
        when(roomRepository.findByNumber(ROOM_DTO.getNumber()))
            .thenReturn(Optional.ofNullable(EXPECTED_ROOM));
        Errors errors = new BeanPropertyBindingResult(new RoomResponseDTO(), "");

        roomValidator.validate(ROOM_DTO, errors);

        verify(roomRepository, times(INVOCATION_NUMBER)).findByNumber(ROOM_DTO.getNumber());
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }

    @Test
    void givenUpdatedLectureRoom_whenValidate_thenNoError() {
        when(roomRepository.findByNumber(UPDATED_ROOM_DTO.getNumber()))
            .thenReturn(Optional.ofNullable(EXPECTED_ROOM));
        when(roomRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_ROOM));
        Errors errors = new BeanPropertyBindingResult(new RoomResponseDTO(), "");

        roomValidator.validate(UPDATED_ROOM_DTO, errors);

        verify(roomRepository, times(INVOCATION_NUMBER)).findByNumber(UPDATED_ROOM_DTO.getNumber());
        verify(roomRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenUpdatedLectureRoom_whenValidate_thenError() {
        when(roomRepository.findByNumber(UPDATED_ROOM_DTO.getNumber()))
            .thenReturn(Optional.ofNullable(EXPECTED_ROOM));
        when(roomRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_UPDATED_ROOM));
        Errors errors = new BeanPropertyBindingResult(new RoomResponseDTO(), "");

        roomValidator.validate(UPDATED_ROOM_DTO, errors);

        verify(roomRepository, times(INVOCATION_NUMBER)).findByNumber(UPDATED_ROOM_DTO.getNumber());
        verify(roomRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }

    @Test
    void givenUpdatedLectureRoom_whenValidate_EntityNotFoundException() {
        when(roomRepository.findByNumber(UPDATED_ROOM_DTO.getNumber()))
            .thenReturn(Optional.ofNullable(EXPECTED_ROOM));
        when(roomRepository.findById(ACTUAL_ID))
            .thenThrow(new EntityNotFoundException());
        Errors errors = new BeanPropertyBindingResult(new RoomResponseDTO(), "");

        assertThrows(EntityNotFoundException.class,
            () -> roomValidator.validate(UPDATED_ROOM_DTO, errors));
    }
}
