package ua.foxminded.university.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.repository.DepartmentRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.ENTITY_NOT_FOUND;
import static ua.foxminded.university.util.validators.ValidatorMessages.EXISTS_MESSAGE;

@Component
public class DepartmentValidator implements Validator {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Department.class.getSimpleName());

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentValidator(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return DepartmentRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        DepartmentRequestDTO updatedDepartment = (DepartmentRequestDTO) target;
        Optional<Department> existedDepartment = departmentRepository.findByName(updatedDepartment.getName());

        if (existedDepartment.isPresent()) {

            if (Objects.isNull(updatedDepartment.getId())) {
                errors.reject("global", format(EXISTS_MESSAGE, Department.class.getSimpleName()));

            } else {
                Department departmentToUpdate = departmentRepository.findById(updatedDepartment.getId())
                    .orElseThrow(() -> new EntityNotFoundException(MESSAGE));
                String existedName = departmentToUpdate.getName();

                if (!existedName.equals(updatedDepartment.getName())) {
                    errors.reject("global", format(EXISTS_MESSAGE, Department.class.getSimpleName()));
                }
            }
        }
    }
}
