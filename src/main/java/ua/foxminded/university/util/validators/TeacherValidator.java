package ua.foxminded.university.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.domain.Teacher;
import ua.foxminded.university.dto.teacher.TeacherRequestDTO;
import ua.foxminded.university.repository.EmployeeRepository;
import ua.foxminded.university.repository.TeacherRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.Objects;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.ENTITY_NOT_FOUND;
import static ua.foxminded.university.util.validators.ValidatorMessages.EXISTS_MESSAGE;

@Component
public class TeacherValidator implements Validator {

    private static final String TEACHER_MESSAGE = format(ENTITY_NOT_FOUND, Teacher.class.getSimpleName());
    private static final String EMPLOYEE_MESSAGE = format(ENTITY_NOT_FOUND, Employee.class.getSimpleName());

    private final TeacherRepository teacherRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public TeacherValidator(TeacherRepository teacherRepository, EmployeeRepository employeeRepository) {
        this.teacherRepository = teacherRepository;
        this.employeeRepository = employeeRepository;
    }


    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return TeacherRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        TeacherRequestDTO updatedTeacher = (TeacherRequestDTO) target;

        if (!Objects.isNull(updatedTeacher.getEmployee().getId())) {
            boolean existsByEmployee = teacherRepository.existsByEmployeeId(updatedTeacher.getEmployee().getId());

            if (existsByEmployee) {

                if (Objects.isNull(updatedTeacher.getId())) {
                    errors.reject("global", format(EXISTS_MESSAGE, Teacher.class.getSimpleName()));

                } else {
                    Teacher teacherToUpdate = teacherRepository.findById(updatedTeacher.getId())
                        .orElseThrow(() -> new EntityNotFoundException(TEACHER_MESSAGE));
                    Employee existedEmployee = teacherToUpdate.getEmployee();
                    Employee updatedTeachersEmployee = employeeRepository
                        .findById(updatedTeacher.getEmployee().getId())
                        .orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_MESSAGE));

                    if (!existedEmployee.equals(updatedTeachersEmployee)) {
                        errors.reject("global", format(EXISTS_MESSAGE, Teacher.class.getSimpleName()));
                    }
                }
            }
        }
    }
}
