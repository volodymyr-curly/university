package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.repository.DurationRepository;
import ua.foxminded.university.domain.Duration;
import ua.foxminded.university.service.interfaces.DurationService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class DurationServiceImpl implements DurationService {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Duration.class.getSimpleName());

    private final DurationRepository durationRepository;

    @Autowired
    public DurationServiceImpl(DurationRepository durationRepository) {
        this.durationRepository = durationRepository;
    }

    @Override
    @Transactional
    public void add(Duration duration) {
        durationRepository.save(duration);
    }

    @Override
    @Transactional
    public void update(int id, Duration updatedDuration) {
        ifNotFoundThenException(id);
        durationRepository.save(updatedDuration);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        durationRepository.deleteById(id);

    }

    @Override
    public Duration find(int id) {
        return durationRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to find duration with id={}", id);
            return new EntityNotFoundException(MESSAGE);
        });
    }

    @Override
    public List<Duration> findAll() {
        List<Duration> durations = durationRepository.findAll();

        if (durations.isEmpty()) {
            log.error("Fail when searching for durations list");
            throw new EntityNotFoundException(NOT_FOUND);
        }

        return durations;
    }

    private void ifNotFoundThenException(int id) {
        if (!durationRepository.existsById(id)) {
            log.error("Duration with id={} not found", id);
            throw new EntityNotFoundException(MESSAGE);
        }
    }
}
