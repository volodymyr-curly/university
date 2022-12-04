package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.repository.DepartmentRepository;
import ua.foxminded.university.service.interfaces.DepartmentService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedDepartments;

@ExtendWith(SpringExtension.class)
@Import(DepartmentServiceImpl.class)
class DepartmentServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Department EXPECTED_DEPARTMENT = setExpectedDepartments().get(0);

    @Autowired
    private DepartmentService departmentService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @Test
    void givenDepartment_whenAddDepartment_thenDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(EXPECTED_DEPARTMENT);

        departmentService.add(EXPECTED_DEPARTMENT);

        verify(departmentRepository, times(INVOCATION_NUMBER)).save(EXPECTED_DEPARTMENT);
    }

    @Test
    void givenDepartment_whenUpdateDepartment_thenUpdatedDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(EXPECTED_DEPARTMENT);
        when(departmentRepository.existsById(ACTUAL_ID)).thenReturn(true);

        departmentService.update(ACTUAL_ID, EXPECTED_DEPARTMENT);

        verify(departmentRepository, times(INVOCATION_NUMBER)).save(EXPECTED_DEPARTMENT);
    }

    @Test
    void givenDepartment_whenDeleteDepartment_thenNoDepartment() {
        doNothing().when(departmentRepository).deleteById(ACTUAL_ID);
        when(departmentRepository.existsById(ACTUAL_ID)).thenReturn(true);

        departmentService.delete(ACTUAL_ID);

        verify(departmentRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenDepartmentId_whenFindDepartment_thenDepartment() {
        when(departmentRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_DEPARTMENT));

        Department actualDepartment = departmentService.find(ACTUAL_ID);

        assertEquals(EXPECTED_DEPARTMENT, actualDepartment);
        verify(departmentRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindDepartments_thenListOfDepartments() {
        when(departmentRepository.findAll()).thenReturn(setExpectedDepartments());

        List<Department> actualDepartments = departmentService.findAll();

        assertEquals(setExpectedDepartments(), actualDepartments);
        verify(departmentRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenFacultyId_whenFindDepartmentByFaculty_thenListOfDepartment() {
        when(departmentRepository.findByFacultyId(ACTUAL_ID)).thenReturn(setExpectedDepartments());

        List<Department> actualDepartments = departmentService.findByFaculty(ACTUAL_ID);

        assertEquals(setExpectedDepartments(), actualDepartments);
        verify(departmentRepository, times(INVOCATION_NUMBER)).findByFacultyId(ACTUAL_ID);
    }

    @Test
    void givenDepartment_whenUpdateDepartment_thenEntityNotFoundException() {
        when(departmentRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> departmentService.update(ACTUAL_ID, EXPECTED_DEPARTMENT));
    }

    @Test
    void givenDepartment_whenDeleteDepartment_thenEntityNotFoundException() {
        when(departmentRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> departmentService.delete(ACTUAL_ID));
    }

    @Test
    void givenDepartmentId_whenFindDepartment_thenEntityNotFoundException() {
        when(departmentRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> departmentService.find(ACTUAL_ID));
    }

    @Test
    void whenFindDepartments_thenEntityNotFoundException() {
        when(departmentRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> departmentService.findAll());
    }

    @Test
    void givenFacultyId_whenFindDepartmentsById_thenServiceException() {
        when(departmentRepository.findByFacultyId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> departmentService.findAll());
    }
}
