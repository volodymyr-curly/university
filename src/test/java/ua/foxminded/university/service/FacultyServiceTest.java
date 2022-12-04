package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Faculty;
import ua.foxminded.university.repository.FacultyRepository;
import ua.foxminded.university.service.interfaces.FacultyService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedFaculties;

@ExtendWith(SpringExtension.class)
@Import(FacultyServiceImpl.class)
class FacultyServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Faculty EXPECTED_FACULTY = setExpectedFaculties().get(0);

    @Autowired
    private FacultyService facultyService;

    @MockBean
    private FacultyRepository facultyRepository;

    @Test
    void givenFaculty_whenAddFaculty_thenFaculty() {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(EXPECTED_FACULTY);

        facultyService.add(EXPECTED_FACULTY);

        verify(facultyRepository, times(INVOCATION_NUMBER)).save(EXPECTED_FACULTY);
    }

    @Test
    void givenFaculty_whenUpdateFaculty_thenUpdatedFaculty() {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(EXPECTED_FACULTY);
        when(facultyRepository.existsById(ACTUAL_ID)).thenReturn(true);

        facultyService.update(ACTUAL_ID, EXPECTED_FACULTY);

        verify(facultyRepository, times(INVOCATION_NUMBER)).save(EXPECTED_FACULTY);
    }

    @Test
    void givenFaculty_whenDeleteFaculty_thenNoFaculty() {
        doNothing().when(facultyRepository).deleteById(ACTUAL_ID);
        when(facultyRepository.existsById(ACTUAL_ID)).thenReturn(true);

        facultyService.delete(ACTUAL_ID);

        verify(facultyRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenFacultyId_whenFindFaculty_thenFaculty() {
        when(facultyRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_FACULTY));

        Faculty actualFaculty = facultyService.find(ACTUAL_ID);

        assertEquals(EXPECTED_FACULTY, actualFaculty);
        verify(facultyRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindFaculties_thenListOfFaculties() {
        when(facultyRepository.findAll()).thenReturn(setExpectedFaculties());

        List<Faculty> actualFaculties = facultyService.findAll();

        assertEquals(setExpectedFaculties(), actualFaculties);
        verify(facultyRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenFaculty_whenUpdateFaculty_thenEntityNotFoundException() {
        when(facultyRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> facultyService.update(ACTUAL_ID, EXPECTED_FACULTY));
    }

    @Test
    void givenFaculty_whenDeleteFaculty_thenEntityNotFoundException() {
        when(facultyRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> facultyService.delete(ACTUAL_ID));
    }

    @Test
    void givenFacultyId_whenFindFaculty_thenEntityNotFoundException() {
        when(facultyRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> facultyService.find(ACTUAL_ID));
    }

    @Test
    void whenFindFaculties_thenEntityNotFoundException() {
        when(facultyRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> facultyService.findAll());
    }
}
