package ua.foxminded.university.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.Faculty;
import ua.foxminded.university.dto.faculty.FacultyRequestDTO;
import ua.foxminded.university.repository.FacultyRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.ENTITY_NOT_FOUND;
import static ua.foxminded.university.util.validators.ValidatorMessages.EXISTS_MESSAGE;

@Component
public class FacultyValidator implements Validator {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Faculty.class.getSimpleName());

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyValidator(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return FacultyRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        FacultyRequestDTO updatedFaculty = (FacultyRequestDTO) target;
        Optional<Faculty> existedFaculty = facultyRepository.findByName(updatedFaculty.getName());

        if (existedFaculty.isPresent()) {

            if (Objects.isNull(updatedFaculty.getId())) {
                errors.reject("global", format(EXISTS_MESSAGE, Faculty.class.getSimpleName()));

            } else {
                Faculty facultyToUpdate = facultyRepository.findById(updatedFaculty.getId())
                    .orElseThrow(() -> new EntityNotFoundException(MESSAGE));
                String existedName = facultyToUpdate.getName();

                if (!existedName.equals(updatedFaculty.getName())) {
                    errors.reject("global", format(EXISTS_MESSAGE, Faculty.class.getSimpleName()));
                }
            }
        }
    }
}
