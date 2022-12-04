package ua.foxminded.university.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.Duration;
import ua.foxminded.university.dto.duration.DurationRequestDTO;
import ua.foxminded.university.repository.DurationRepository;

import java.util.Objects;

import static java.lang.String.format;
import static ua.foxminded.university.util.validators.ValidatorMessages.EXISTS_MESSAGE;

@Component
public class DurationValidator implements Validator {

    private final DurationRepository durationRepository;

    @Autowired
    public DurationValidator(DurationRepository durationRepository) {
        this.durationRepository = durationRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return DurationRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        DurationRequestDTO updatedDuration = (DurationRequestDTO) target;

        if (durationRepository.existsByStartTime(updatedDuration.getStartTime()) ||
            durationRepository.existsByEndTime(updatedDuration.getEndTime())) {
            errors.reject("global", format(EXISTS_MESSAGE, Duration.class.getSimpleName()));
        }
    }
}

