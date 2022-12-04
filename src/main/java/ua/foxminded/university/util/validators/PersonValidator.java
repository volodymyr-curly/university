package ua.foxminded.university.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.Person;
import ua.foxminded.university.dto.person.PersonDTO;
import ua.foxminded.university.repository.PersonRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.ENTITY_NOT_FOUND;
import static ua.foxminded.university.util.validators.ValidatorMessages.EMAIL_EXISTS_MESSAGE;

@Component
public class PersonValidator implements Validator {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Person.class.getSimpleName());

    private final PersonRepository personRepository;

    @Autowired
    public PersonValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return PersonDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        PersonDTO updatedPerson = (PersonDTO) target;
        Optional<Person> existedPerson = personRepository.findByEmail(updatedPerson.getEmail());

        if (existedPerson.isPresent()) {

            if (Objects.isNull(updatedPerson.getId())) {
                errors.rejectValue("email", "", EMAIL_EXISTS_MESSAGE);

            } else {
                Person personToUpdate = personRepository.findById(updatedPerson.getId())
                    .orElseThrow(() -> new EntityNotFoundException(MESSAGE));
                String existedEmail = personToUpdate.getEmail();

                if (!existedEmail.equals(updatedPerson.getEmail())) {
                    errors.rejectValue("email", "", EMAIL_EXISTS_MESSAGE);
                }
            }
        }
    }
}
