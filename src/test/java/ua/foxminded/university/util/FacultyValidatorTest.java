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
import ua.foxminded.university.domain.Faculty;
import ua.foxminded.university.dto.faculty.FacultyRequestDTO;
import ua.foxminded.university.dto.faculty.FacultyResponseDTO;
import ua.foxminded.university.repository.FacultyRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;
import ua.foxminded.university.util.validators.FacultyValidator;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.DTOData.setFacultyDTO;
import static ua.foxminded.university.data.DTOData.setUpdatedFacultyDTO;
import static ua.foxminded.university.data.EntityData.setExpectedFaculties;

@ExtendWith(SpringExtension.class)
@Import(FacultyValidator.class)
class FacultyValidatorTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final int EXPECTED_ERRORS_COUNT = 1;
    private static final String EXIST_MESSAGE = "Faculty is already exist";
    private static final Faculty EXPECTED_FACULTY = setExpectedFaculties().get(0);
    private static final Faculty EXPECTED_UPDATED_FACULTY = setExpectedFaculties().get(1);
    private static final FacultyRequestDTO FACULTY_DTO = setFacultyDTO();
    private static final FacultyRequestDTO UPDATED_FACULTY_DTO = setUpdatedFacultyDTO();

    @Autowired
    private Validator facultyValidator;

    @MockBean
    private FacultyRepository facultyRepository;

    @Test
    void whenValidate_thenSupportsClass() {
        assertTrue(facultyValidator.supports(FacultyRequestDTO.class));
        assertFalse(facultyValidator.supports(Object.class));
    }

    @Test
    void givenNewFaculty_whenValidate_thenNoError() {
        when(facultyRepository.findByName(FACULTY_DTO.getName())).thenReturn(Optional.empty());
        Errors errors = new BeanPropertyBindingResult(new FacultyResponseDTO(), "");

        facultyValidator.validate(FACULTY_DTO, errors);

        verify(facultyRepository, times(INVOCATION_NUMBER)).findByName(FACULTY_DTO.getName());
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenNewFaculty_whenValidate_thenError() {
        when(facultyRepository.findByName(FACULTY_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_FACULTY));
        Errors errors = new BeanPropertyBindingResult(new FacultyResponseDTO(), "");

        facultyValidator.validate(FACULTY_DTO, errors);

        verify(facultyRepository, times(INVOCATION_NUMBER)).findByName(FACULTY_DTO.getName());
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }

    @Test
    void givenUpdatedFaculty_whenValidate_thenNoError() {
        when(facultyRepository.findByName(UPDATED_FACULTY_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_FACULTY));
        when(facultyRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_FACULTY));
        Errors errors = new BeanPropertyBindingResult(new FacultyResponseDTO(), "");

        facultyValidator.validate(UPDATED_FACULTY_DTO, errors);

        verify(facultyRepository, times(INVOCATION_NUMBER)).findByName(UPDATED_FACULTY_DTO.getName());
        verify(facultyRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenUpdatedFaculty_whenValidate_thenError() {
        when(facultyRepository.findByName(UPDATED_FACULTY_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_FACULTY));
        when(facultyRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_UPDATED_FACULTY));
        Errors errors = new BeanPropertyBindingResult(new FacultyResponseDTO(), "");

        facultyValidator.validate(UPDATED_FACULTY_DTO, errors);

        verify(facultyRepository, times(INVOCATION_NUMBER)).findByName(UPDATED_FACULTY_DTO.getName());
        verify(facultyRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }

    @Test
    void givenUpdatedFaculty_whenValidate_EntityNotFoundException() {
        when(facultyRepository.findByName(UPDATED_FACULTY_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_FACULTY));
        when(facultyRepository.findById(ACTUAL_ID))
            .thenThrow(new EntityNotFoundException());
        Errors errors = new BeanPropertyBindingResult(new FacultyResponseDTO(), "");

        assertThrows(EntityNotFoundException.class,
            () -> facultyValidator.validate(UPDATED_FACULTY_DTO, errors));
    }
}
