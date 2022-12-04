package ua.foxminded.university.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.Group;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.repository.GroupRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.ENTITY_NOT_FOUND;
import static ua.foxminded.university.util.validators.ValidatorMessages.*;

@Component
public class GroupValidator implements Validator {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Group.class.getSimpleName());

    private final GroupRepository groupRepository;

    @Autowired
    public GroupValidator(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return GroupRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate( @NonNull Object target, @NonNull Errors errors) {
        GroupRequestDTO updatedGroup = (GroupRequestDTO) target;
        Optional<Group> existedGroup = groupRepository.findByName(updatedGroup.getName());

        if (existedGroup.isPresent()) {

            if (Objects.isNull(updatedGroup.getId())) {
                errors.reject("global", format(EXISTS_MESSAGE, Group.class.getSimpleName()));

            } else {
                Group groupToUpdate = groupRepository.findById(updatedGroup.getId())
                    .orElseThrow(() -> new EntityNotFoundException(MESSAGE));
                String existedName = groupToUpdate.getName();

                if (!existedName.equals(updatedGroup.getName())) {
                    errors.reject("global", format(EXISTS_MESSAGE, Group.class.getSimpleName()));
                }

                if (Objects.isNull(updatedGroup.getLectures())) {
                    errors.rejectValue("lectures", "", NOT_EMPTY_MESSAGE);
                }
            }
        }
    }
}
