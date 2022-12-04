package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.domain.*;
import ua.foxminded.university.repository.AddressRepository;
import ua.foxminded.university.repository.EmployeeRepository;
import ua.foxminded.university.repository.PersonRepository;
import ua.foxminded.university.service.interfaces.EmployeeService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static ua.foxminded.university.domain.Role.*;
import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private static final String NOT_FOUND_MESSAGE = format(ENTITY_NOT_FOUND, Person.class.getSimpleName());
    private static final String EXISTS_MESSAGE = format(ENTITY_EXISTS, Person.class.getSimpleName());

    private final EmployeeRepository employeeRepository;
    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, AddressRepository addressRepository, PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void add(Employee employee) {
        ifAlreadyExistThenException(employee);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        if (employee.getRole() == null) {
            employee.setRole(ROLE_EMPLOYEE);
        }

        employeeRepository.save(employee);
        employee.getAddress().setPerson(employee);
    }

    @Override
    @Transactional
    public void update(int id, Employee updatedEmployee) {
        ifNotFoundThenException(id);
        ifAlreadyExistThenException(updatedEmployee);
        Address addressToUpdate = addressRepository.findByPersonId(id);
        updatedEmployee.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
        updatedEmployee.getAddress().setId(addressToUpdate.getId());
        updatedEmployee.getAddress().setPerson(updatedEmployee);
        employeeRepository.save(updatedEmployee);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee find(int id) {
        return employeeRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to find employee with id={}", id);
            return new EntityNotFoundException(NOT_FOUND_MESSAGE);
        });
    }


    @Override
    public List<Employee> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        ifListIsEmptyThenException(employees);
        return employees;
    }

    @Override
    public List<Employee> findByDepartment(int id) {
        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        ifListIsEmptyThenException(employees);
        return employees;
    }

    @Override
    public List<Employee> findNotTeachers() {
        return employeeRepository.findNotTeachers();
    }

    private void ifAlreadyExistThenException(Employee employee) {
        if (employee.getId() == 0) {

            if (personRepository.findAll().contains(employee)) {
                log.error("Exists {}", employee);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }

        } else {

            if ((!personRepository.findById(employee.getId()).get().equals(employee))
                && (personRepository.findAll().contains(employee))) {
                log.error("Exists {}", employee);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }
        }
    }

    private void ifNotFoundThenException(int id) {
        if (!employeeRepository.existsById(id)) {
            log.error("Employee with id={} not found", id);
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private void ifListIsEmptyThenException(List<Employee> employees) {
        if (employees.isEmpty()) {
            log.error("Fail when searching for employees list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }
}
