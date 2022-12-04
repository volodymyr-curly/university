package ua.foxminded.university.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.dto.student.StudentRequestDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ua.foxminded.university.data.DTOData.*;

@ExtendWith(SpringExtension.class)
class PasswordValidatorTest {

    private static final int EXPECTED_CONSTRAINT_NUMBER = 1;
    private static final String VALID_PASSWORD = "password";
    private static final String SHORT_PASSWORD = "pass";
    private static final String LONG_PASSWORD = "password_password";
    private static final String PASSWORD_WITH_WHITESPACE = "pass word";
    private static final String SHORT_MESSAGE = "Password must be 5 or more characters in length.";
    private static final String LONG_MESSAGE = "Password must be no more than 10 characters in length.";
    private static final String WHITESPACE_MESSAGE = "Password contains a whitespace character.";
    private static final StudentRequestDTO STUDENT_DTO = setStudentDTO();

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void givenStudent_whenValidation_thenValidPassword() {
        STUDENT_DTO.setPassword(VALID_PASSWORD);

        Set<ConstraintViolation<StudentRequestDTO>> constraintViolations = validator.validate(STUDENT_DTO);

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void givenStudent_whenValidation_thenShortPassword() {
        STUDENT_DTO.setPassword(SHORT_PASSWORD);

        Set<ConstraintViolation<StudentRequestDTO>> constraintViolations = validator.validate(STUDENT_DTO);

        List<String> actualMessages = constraintViolations.stream()
            .map(ConstraintViolation::getMessageTemplate).toList();
        assertEquals(EXPECTED_CONSTRAINT_NUMBER, constraintViolations.size());
        assertEquals(SHORT_MESSAGE, actualMessages.get(0));
    }

    @Test
    void givenStudent_whenValidation_thenLongPassword() {
        STUDENT_DTO.setPassword(LONG_PASSWORD);

        Set<ConstraintViolation<StudentRequestDTO>> constraintViolations = validator.validate(STUDENT_DTO);

        List<String> actualMessages = constraintViolations.stream()
            .map(ConstraintViolation::getMessageTemplate).toList();
        assertEquals(EXPECTED_CONSTRAINT_NUMBER, constraintViolations.size());
        assertEquals(LONG_MESSAGE, actualMessages.get(0));
    }

    @Test
    void givenStudent_whenValidation_thenPasswordWithWhitespace() {
        STUDENT_DTO.setPassword(PASSWORD_WITH_WHITESPACE);

        Set<ConstraintViolation<StudentRequestDTO>> constraintViolations = validator.validate(STUDENT_DTO);

        List<String> actualMessages = constraintViolations.stream()
            .map(ConstraintViolation::getMessageTemplate).toList();
        assertEquals(EXPECTED_CONSTRAINT_NUMBER, constraintViolations.size());
        assertEquals(WHITESPACE_MESSAGE, actualMessages.get(0));
    }
}
