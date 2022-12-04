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
import ua.foxminded.university.dto.duration.DurationRequestDTO;
import ua.foxminded.university.dto.duration.DurationResponseDTO;
import ua.foxminded.university.repository.DurationRepository;
import ua.foxminded.university.util.validators.DurationValidator;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.DTOData.setDurationDTO;

@ExtendWith(SpringExtension.class)
@Import(DurationValidator.class)
class DurationValidatorTest {

    private static final int INVOCATION_NUMBER = 1;
    private static final int EXPECTED_ERRORS_COUNT = 1;
    private static final String EXIST_MESSAGE = "Duration is already exist";
    private static final DurationRequestDTO DURATION_DTO = setDurationDTO();

    @Autowired
    private Validator durationValidator;

    @MockBean
    private DurationRepository durationRepository;

    @Test
    void whenValidate_thenSupportsClass() {
        assertTrue(durationValidator.supports(DurationRequestDTO.class));
        assertFalse(durationValidator.supports(Object.class));
    }

    @Test
    void givenNewDuration_whenValidate_thenNoError() {
        when(durationRepository.existsByStartTime(DURATION_DTO.getStartTime())).thenReturn(false);
        when(durationRepository.existsByEndTime(DURATION_DTO.getEndTime())).thenReturn(false);
        Errors errors = new BeanPropertyBindingResult(new DurationResponseDTO(), "");

        durationValidator.validate(DURATION_DTO, errors);

        verify(durationRepository, times(INVOCATION_NUMBER)).existsByStartTime(DURATION_DTO.getStartTime());
        verify(durationRepository, times(INVOCATION_NUMBER)).existsByEndTime(DURATION_DTO.getEndTime());
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenNewDurationWithExistedStartTime_whenValidate_thenError() {
        when(durationRepository.existsByStartTime(DURATION_DTO.getStartTime())).thenReturn(true);
        Errors errors = new BeanPropertyBindingResult(new DurationResponseDTO(), "");

        durationValidator.validate(DURATION_DTO, errors);

        verify(durationRepository, times(INVOCATION_NUMBER)).existsByStartTime(DURATION_DTO.getStartTime());
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }

    @Test
    void givenNewDurationWithExistedEndTime_whenValidate_thenError() {
        when(durationRepository.existsByStartTime(DURATION_DTO.getStartTime())).thenReturn(false);
        when(durationRepository.existsByEndTime(DURATION_DTO.getEndTime())).thenReturn(true);
        Errors errors = new BeanPropertyBindingResult(new DurationResponseDTO(), "");

        durationValidator.validate(DURATION_DTO, errors);

        verify(durationRepository, times(INVOCATION_NUMBER)).existsByStartTime(DURATION_DTO.getStartTime());
        verify(durationRepository, times(INVOCATION_NUMBER)).existsByEndTime(DURATION_DTO.getEndTime());
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }
}
