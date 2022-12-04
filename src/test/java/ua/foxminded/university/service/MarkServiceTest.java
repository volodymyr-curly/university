package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Mark;
import ua.foxminded.university.domain.MarkValue;
import ua.foxminded.university.repository.MarkRepository;
import ua.foxminded.university.service.interfaces.MarkService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(SpringExtension.class)
@Import(MarkServiceImpl.class)
class MarkServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Mark EXPECTED_MARK = setExpectedMarks().get(0);

    @Autowired
    private MarkService markService;

    @MockBean
    private MarkRepository markRepository;

    @Test
    void givenMark_whenAddMark_thenMark() {
        when(markRepository.existsSameMark(any(MarkValue.class), anyInt(), anyInt()))
            .thenReturn(false);
        when(markRepository.save(any(Mark.class))).thenReturn(EXPECTED_MARK);
        EXPECTED_MARK.setId(0);

        markService.add(EXPECTED_MARK);

        verify(markRepository, times(INVOCATION_NUMBER)).save(EXPECTED_MARK);
        verify(markRepository, times(INVOCATION_NUMBER))
            .existsSameMark(any(MarkValue.class), anyInt(), anyInt());
    }

    @Test
    void givenMark_whenUpdateMark_thenUpdatedMark() {
        when(markRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(markRepository.existsSameMark(any(MarkValue.class), anyInt(), anyInt()))
            .thenReturn(false);
        when(markRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(setExpectedMarks().get(1)));
        when(markRepository.save(any(Mark.class))).thenReturn(EXPECTED_MARK);

        markService.update(ACTUAL_ID, EXPECTED_MARK);

        verify(markRepository, times(INVOCATION_NUMBER)).save(EXPECTED_MARK);
        verify(markRepository, times(INVOCATION_NUMBER))
            .existsSameMark(any(MarkValue.class), anyInt(), anyInt());
    }

    @Test
    void givenMark_whenDeleteMark_thenNoMark() {
        doNothing().when(markRepository).deleteById(ACTUAL_ID);
        when(markRepository.existsById(ACTUAL_ID)).thenReturn(true);

        markService.delete(ACTUAL_ID);

        verify(markRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenMarkId_whenFindMark_thenMark() {
        when(markRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_MARK));

        Mark actualMark = markService.find(ACTUAL_ID);

        assertEquals(EXPECTED_MARK, actualMark);
        verify(markRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindMarks_thenListOfMarks() {
        when(markRepository.findAll()).thenReturn(setExpectedMarks());

        List<Mark> actualMarks = markService.findAll();

        assertEquals(setExpectedMarks(), actualMarks);
        verify(markRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenSubjectId_whenFindMarkBySubject_thenListOfMarks() {
        when(markRepository.findBySubjectId(ACTUAL_ID)).thenReturn(setExpectedMarks());

        List<Mark> actualMarks = markService.findBySubject(ACTUAL_ID);

        assertEquals(setExpectedMarks(), actualMarks);
        verify(markRepository, times(INVOCATION_NUMBER)).findBySubjectId(ACTUAL_ID);
    }

    @Test
    void givenStudentId_whenFindMarkByStudent_thenListOfMarks() {
        when(markRepository.findByStudentId(ACTUAL_ID)).thenReturn(setExpectedMarks());

        List<Mark> actualMarks = markService.findByStudent(ACTUAL_ID);

        assertEquals(setExpectedMarks(), actualMarks);
        verify(markRepository, times(INVOCATION_NUMBER)).findByStudentId(ACTUAL_ID);
    }

    @Test
    void givenMark_whenAddMark_thenEntityExistsException() {
        when(markRepository.existsSameMark(any(MarkValue.class), anyInt(), anyInt()))
            .thenReturn(true);
        EXPECTED_MARK.setId(0);

        assertThrows(EntityExistsException.class, () -> markService.add(EXPECTED_MARK));
    }

    @Test
    void givenMark_whenUpdateMark_thenEntityExistsException() {
        when(markRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(markRepository.existsSameMark(any(MarkValue.class), anyInt(), anyInt()))
            .thenReturn(true);
        when(markRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(setExpectedMarks().get(1)));


        assertThrows(EntityExistsException.class, () -> markService.update(ACTUAL_ID, EXPECTED_MARK));
    }

    @Test
    void givenMark_whenUpdateMark_thenEntityNotFoundException() {
        when(markRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> markService.update(ACTUAL_ID, EXPECTED_MARK));
    }

    @Test
    void givenMark_whenDeleteMark_thenEntityNotFoundException() {
        when(markRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> markService.delete(ACTUAL_ID));
    }

    @Test
    void givenMarkId_whenFindMark_thenEntityNotFoundException() {
        when(markRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> markService.find(ACTUAL_ID));
    }

    @Test
    void whenFindMarks_thenEntityNotFoundException() {
        when(markRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> markService.findAll());
    }

    @Test
    void givenStudentId_whenFindMarksById_thenEntityNotFoundException() {
        when(markRepository.findByStudentId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> markService.findByStudent(ACTUAL_ID));
    }

    @Test
    void givenSubjectId_whenFindMarksById_thenEntityNotFoundException() {
        when(markRepository.findBySubjectId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> markService.findBySubject(ACTUAL_ID));
    }
}
