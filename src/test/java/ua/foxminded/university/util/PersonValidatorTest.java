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
import ua.foxminded.university.domain.Person;
import ua.foxminded.university.dto.person.PersonDTO;
import ua.foxminded.university.dto.student.StudentRequestDTO;
import ua.foxminded.university.repository.PersonRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;
import ua.foxminded.university.util.validators.PersonValidator;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.DTOData.*;
import static ua.foxminded.university.data.EntityData.setExpectedStudents;

@ExtendWith(SpringExtension.class)
@Import(PersonValidator.class)
class PersonValidatorTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final int EXPECTED_ERRORS_COUNT = 1;
    private static final String EXIST_MESSAGE = "This email is already taken";
    private static final Person EXPECTED_PERSON = setExpectedStudents().get(0);
    private static final Person EXPECTED_UPDATED_PERSON = setExpectedStudents().get(1);
    private static final PersonDTO PERSON_DTO = setPersonDTO();
    private static final PersonDTO UPDATED_PERSON_DTO = setUpdatedPersonDTO();

    @Autowired
    private Validator personValidator;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void whenValidate_thenSupportsClass() {
        assertTrue(personValidator.supports(PersonDTO.class));
        assertFalse(personValidator.supports(Object.class));
    }

    @Test
    void givenNewPerson_whenValidate_thenNoError() {
        when(personRepository.findByEmail(PERSON_DTO.getEmail())).thenReturn(Optional.empty());
        Errors errors = new BeanPropertyBindingResult(new StudentRequestDTO(), "");

        personValidator.validate(PERSON_DTO, errors);

        verify(personRepository, times(INVOCATION_NUMBER)).findByEmail(PERSON_DTO.getEmail());
        assertNull(errors.getFieldError("email"));
    }

    @Test
    void givenNewPerson_whenValidate_thenError() {
        when(personRepository.findByEmail(PERSON_DTO.getEmail()))
            .thenReturn(Optional.ofNullable(EXPECTED_PERSON));
        Errors errors = new BeanPropertyBindingResult(new StudentRequestDTO(), "");

        personValidator.validate(PERSON_DTO, errors);

        verify(personRepository, times(INVOCATION_NUMBER)).findByEmail(PERSON_DTO.getEmail());
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getFieldError("email")).getDefaultMessage());
    }

    @Test
    void givenUpdatedPerson_whenValidate_thenNoError() {
        when(personRepository.findByEmail(UPDATED_PERSON_DTO.getEmail()))
            .thenReturn(Optional.ofNullable(EXPECTED_PERSON));
        when(personRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_PERSON));
        Errors errors = new BeanPropertyBindingResult(new StudentRequestDTO(), "");

        personValidator.validate(UPDATED_PERSON_DTO, errors);

        verify(personRepository, times(INVOCATION_NUMBER)).findByEmail(UPDATED_PERSON_DTO.getEmail());
        verify(personRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertNull(errors.getFieldError("email"));
    }

    @Test
    void givenUpdatedPerson_whenValidate_thenError() {
        when(personRepository.findByEmail(UPDATED_PERSON_DTO.getEmail()))
            .thenReturn(Optional.ofNullable(EXPECTED_PERSON));
        when(personRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_UPDATED_PERSON));
        Errors errors = new BeanPropertyBindingResult(new StudentRequestDTO(), "");

        personValidator.validate(UPDATED_PERSON_DTO, errors);

        verify(personRepository, times(INVOCATION_NUMBER)).findByEmail(UPDATED_PERSON_DTO.getEmail());
        verify(personRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getFieldError("email")).getDefaultMessage());
    }

    @Test
    void givenUpdatedPerson_whenValidate_EntityNotFoundException() {
        when(personRepository.findByEmail(UPDATED_PERSON_DTO.getEmail()))
            .thenReturn(Optional.ofNullable(EXPECTED_PERSON));
        when(personRepository.findById(ACTUAL_ID))
            .thenThrow(new EntityNotFoundException());
        Errors errors = new BeanPropertyBindingResult(new StudentRequestDTO(), "");

        assertThrows(EntityNotFoundException.class,
            () -> personValidator.validate(UPDATED_PERSON_DTO, errors));
    }
}
