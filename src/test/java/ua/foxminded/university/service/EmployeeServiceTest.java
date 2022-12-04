package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.repository.AddressRepository;
import ua.foxminded.university.repository.EmployeeRepository;
import ua.foxminded.university.repository.PersonRepository;
import ua.foxminded.university.service.interfaces.EmployeeService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(SpringExtension.class)
@Import(EmployeeServiceImpl.class)
class EmployeeServiceTest {

    private static final int ACTUAL_ID = 5;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Employee EXPECTED_EMPLOYEE = setExpectedEmployees().get(0);

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void givenEmployee_whenAddEmployee_thenEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(EXPECTED_EMPLOYEE);
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(EXPECTED_EMPLOYEE.getPassword())).thenReturn(EXPECTED_EMPLOYEE.getPassword());
        EXPECTED_EMPLOYEE.setId(0);

        employeeService.add(EXPECTED_EMPLOYEE);

        verify(employeeRepository, times(INVOCATION_NUMBER)).save(EXPECTED_EMPLOYEE);
        verify(personRepository, times(INVOCATION_NUMBER)).findAll();
        verify(passwordEncoder, times(INVOCATION_NUMBER)).encode(EXPECTED_EMPLOYEE.getPassword());
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenUpdatedEmployee() {
        when(addressRepository.findByPersonId(ACTUAL_ID)).thenReturn(setExpectedAddresses().get(0));
        when(employeeRepository.save(any(Employee.class))).thenReturn(EXPECTED_EMPLOYEE);
        when(employeeRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(personRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_EMPLOYEE));
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(EXPECTED_EMPLOYEE.getPassword())).thenReturn(EXPECTED_EMPLOYEE.getPassword());

        employeeService.update(ACTUAL_ID, EXPECTED_EMPLOYEE);

        verify(addressRepository, times(INVOCATION_NUMBER)).findByPersonId(ACTUAL_ID);
        verify(employeeRepository, times(INVOCATION_NUMBER)).save(any(Employee.class));
        verify(employeeRepository, times(INVOCATION_NUMBER)).existsById(ACTUAL_ID);
        verify(passwordEncoder, times(INVOCATION_NUMBER)).encode(EXPECTED_EMPLOYEE.getPassword());
    }

    @Test
    void givenEmployee_whenDeleteEmployee_thenNoEmployee() {
        doNothing().when(employeeRepository).deleteById(ACTUAL_ID);
        when(employeeRepository.existsById(ACTUAL_ID)).thenReturn(true);

        employeeService.delete(ACTUAL_ID);

        verify(employeeRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenEmployeeId_whenFindEmployee_thenEmployee() {
        when(employeeRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_EMPLOYEE));

        Employee actualEmployee = employeeService.find(ACTUAL_ID);

        assertEquals(EXPECTED_EMPLOYEE, actualEmployee);
        verify(employeeRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindEmployees_thenListOfEmployees() {
        when(employeeRepository.findAll()).thenReturn(setExpectedEmployees());

        List<Employee> actualEmployees = employeeService.findAll();

        assertEquals(setExpectedEmployees(), actualEmployees);
        verify(employeeRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenDepartmentId_whenFindEmployeeByDepartment_thenListOfEmployee() {
        when(employeeRepository.findByDepartmentId(ACTUAL_ID)).thenReturn(setExpectedEmployees());

        List<Employee> actualEmployees = employeeService.findByDepartment(ACTUAL_ID);

        assertEquals(setExpectedEmployees(), actualEmployees);
        verify(employeeRepository, times(INVOCATION_NUMBER)).findByDepartmentId(ACTUAL_ID);
    }

    @Test
    void whenFindNotTeachers_thenListOfEmployees() {
        when(employeeRepository.findNotTeachers()).thenReturn(setExpectedEmployees());

        List<Employee> actualEmployees = employeeService.findNotTeachers();

        assertEquals(setExpectedEmployees(), actualEmployees);
        verify(employeeRepository, times(INVOCATION_NUMBER)).findNotTeachers();
    }

    @Test
    void givenEmployee_whenAddEmployee_thenEntityExistsException() {
        EXPECTED_EMPLOYEE.setId(0);
        when(personRepository.findAll()).thenReturn(Collections.singletonList(EXPECTED_EMPLOYEE));

        assertThrows(EntityExistsException.class, () -> employeeService.add(EXPECTED_EMPLOYEE));
    }


    @Test
    void givenEmployee_whenUpdateEmployee_thenEntityExistsException() {
        when(employeeRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(personRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(setExpectedEmployees().get(1)));
        when(personRepository.findAll()).thenReturn(Collections.singletonList(EXPECTED_EMPLOYEE));

        assertThrows(EntityExistsException.class, () -> employeeService.update(ACTUAL_ID, EXPECTED_EMPLOYEE));
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenEntityNotFoundException() {
        when(employeeRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> employeeService.update(ACTUAL_ID, EXPECTED_EMPLOYEE));
    }

    @Test
    void givenEmployee_whenDeleteEmployee_thenEntityNotFoundException() {
        when(employeeRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> employeeService.delete(ACTUAL_ID));
    }

    @Test
    void givenEmployeeId_whenFindEmployee_thenEntityNotFoundException() {
        when(employeeRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> employeeService.find(ACTUAL_ID));
    }

    @Test
    void whenFindEmployees_thenEntityNotFoundException() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> employeeService.findAll());
    }

    @Test
    void givenDepartmentId_whenFindEmployeesById_thenEntityNotFoundException() {
        when(employeeRepository.findByDepartmentId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> employeeService.findByDepartment(ACTUAL_ID));
    }
}
