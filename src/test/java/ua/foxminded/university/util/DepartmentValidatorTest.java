package ua.foxminded.university.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.*;
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.repository.DepartmentRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;
import ua.foxminded.university.util.validators.DepartmentValidator;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.DTOData.*;
import static ua.foxminded.university.data.EntityData.setExpectedDepartments;

@ExtendWith(SpringExtension.class)
@Import(DepartmentValidator.class)
class DepartmentValidatorTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final int EXPECTED_ERRORS_COUNT = 1;
    private static final String EXIST_MESSAGE = "Department is already exist";
    private static final Department EXPECTED_DEPARTMENT = setExpectedDepartments().get(0);
    private static final Department EXPECTED_UPDATED_DEPARTMENT = setExpectedDepartments().get(1);
    private static final DepartmentRequestDTO DEPARTMENT_DTO = setDepartmentRequestDTO();
    private static final DepartmentRequestDTO UPDATED_DEPARTMENT_DTO = setUpdatedDepartmentDTO();

    @Autowired
    private Validator departmentValidator;

    @MockBean
    private DepartmentRepository departmentRepository;

    @Test
    void whenValidate_thenSupportsClass() {
        assertTrue(departmentValidator.supports(DepartmentRequestDTO.class));
        assertFalse(departmentValidator.supports(Object.class));
    }

    @Test
    void givenNewDepartment_whenValidate_thenNoError() {
        when(departmentRepository.findByName(DEPARTMENT_DTO.getName())).thenReturn(Optional.empty());
        Errors errors = new BeanPropertyBindingResult(new DepartmentRequestDTO(), "");

        departmentValidator.validate(DEPARTMENT_DTO, errors);

        verify(departmentRepository, times(INVOCATION_NUMBER)).findByName(DEPARTMENT_DTO.getName());
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenNewDepartment_whenValidate_thenError() {
        when(departmentRepository.findByName(DEPARTMENT_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_DEPARTMENT));
        Errors errors = new BeanPropertyBindingResult(new DepartmentRequestDTO(), "");

        departmentValidator.validate(DEPARTMENT_DTO, errors);

        verify(departmentRepository, times(INVOCATION_NUMBER)).findByName(DEPARTMENT_DTO.getName());
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }

    @Test
    void givenUpdatedDepartment_whenValidate_thenNoError() {
        when(departmentRepository.findByName(UPDATED_DEPARTMENT_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_DEPARTMENT));
        when(departmentRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_DEPARTMENT));
        Errors errors = new BeanPropertyBindingResult(new DepartmentRequestDTO(), "");

        departmentValidator.validate(UPDATED_DEPARTMENT_DTO, errors);

        verify(departmentRepository, times(INVOCATION_NUMBER)).findByName(UPDATED_DEPARTMENT_DTO.getName());
        verify(departmentRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenUpdatedDepartment_whenValidate_thenError() {
        when(departmentRepository.findByName(UPDATED_DEPARTMENT_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_DEPARTMENT));
        when(departmentRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_UPDATED_DEPARTMENT));
        Errors errors = new BeanPropertyBindingResult(new DepartmentRequestDTO(), "");

        departmentValidator.validate(UPDATED_DEPARTMENT_DTO, errors);

        verify(departmentRepository, times(INVOCATION_NUMBER)).findByName(UPDATED_DEPARTMENT_DTO.getName());
        verify(departmentRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }

    @Test
    void givenUpdatedDepartment_whenValidate_EntityNotFoundException() {
        when(departmentRepository.findByName(UPDATED_DEPARTMENT_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_DEPARTMENT));
        when(departmentRepository.findById(ACTUAL_ID))
            .thenThrow(new EntityNotFoundException());
        Errors errors = new BeanPropertyBindingResult(new DepartmentRequestDTO(), "");

        assertThrows(EntityNotFoundException.class,
            () -> departmentValidator.validate(UPDATED_DEPARTMENT_DTO, errors));
    }
}
