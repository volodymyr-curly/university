package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Duration;
import ua.foxminded.university.repository.DurationRepository;
import ua.foxminded.university.service.interfaces.DurationService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedDurations;

@ExtendWith(SpringExtension.class)
@Import(DurationServiceImpl.class)
class DurationServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Duration EXPECTED_DURATION = setExpectedDurations().get(0);

    @Autowired
    private DurationService durationService;

    @MockBean
    private DurationRepository durationRepository;

    @Test
    void givenDuration_whenAddDuration_thenDuration() {
        when(durationRepository.save(any(Duration.class))).thenReturn(EXPECTED_DURATION);

        durationService.add(EXPECTED_DURATION);

        verify(durationRepository, times(INVOCATION_NUMBER)).save(EXPECTED_DURATION);
    }

    @Test
    void givenDuration_whenUpdateDuration_thenUpdatedDuration() {
        when(durationRepository.save(any(Duration.class))).thenReturn(EXPECTED_DURATION);
        when(durationRepository.existsById(ACTUAL_ID)).thenReturn(true);

        durationService.update(ACTUAL_ID, EXPECTED_DURATION);

        verify(durationRepository, times(INVOCATION_NUMBER)).save(EXPECTED_DURATION);
    }

    @Test
    void givenDuration_whenDeleteDuration_thenNoDuration() {
        doNothing().when(durationRepository).deleteById(ACTUAL_ID);
        when(durationRepository.existsById(ACTUAL_ID)).thenReturn(true);

        durationService.delete(ACTUAL_ID);

        verify(durationRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenDurationId_whenFindDuration_thenDuration() {
        when(durationRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_DURATION));

        Duration actualDuration = durationService.find(ACTUAL_ID);

        assertEquals(EXPECTED_DURATION, actualDuration);
        verify(durationRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindDurations_thenListOfDurations() {
        when(durationRepository.findAll()).thenReturn(setExpectedDurations());

        List<Duration> actualDurations = durationService.findAll();

        assertEquals(setExpectedDurations(), actualDurations);
        verify(durationRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenDuration_whenUpdateDuration_thenEntityNotFoundException() {
        when(durationRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> durationService.update(ACTUAL_ID, EXPECTED_DURATION));
    }

    @Test
    void givenDuration_whenDeleteDuration_thenEntityNotFoundException() {
        when(durationRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> durationService.delete(ACTUAL_ID));
    }

    @Test
    void givenDurationId_whenFindDuration_thenEntityNotFoundException() {
        when(durationRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> durationService.find(ACTUAL_ID));
    }

    @Test
    void whenFindDurations_thenEntityNotFoundException() {
        when(durationRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> durationService.findAll());
    }
}
