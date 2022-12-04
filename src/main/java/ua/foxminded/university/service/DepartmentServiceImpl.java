package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.repository.DepartmentRepository;
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.service.interfaces.DepartmentService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Department.class.getSimpleName());

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public void add(Department department) {
        departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void update(int id, Department updatedDepartment) {
        ifNotFoundThenException(id);
        departmentRepository.save(updatedDepartment);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        departmentRepository.deleteById(id);
    }

    @Override
    public Department find(int id) {
        return departmentRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to find department with id={}", id);
            return new EntityNotFoundException(MESSAGE);
        });
    }

    @Override
    public List<Department> findAll() {
        List <Department> departments = departmentRepository.findAll();
        ifListIsEmptyThenException(departments);
        return departments;
    }

    @Override
    public List<Department> findByFaculty(int id) {
        List <Department> departments = departmentRepository.findByFacultyId(id);
        ifListIsEmptyThenException(departments);
        return departments;
    }

    private void ifNotFoundThenException(int id) {
        if (!departmentRepository.existsById(id)) {
            log.error("Department with id={} not found", id);
            throw new EntityNotFoundException(MESSAGE);
        }
    }

    private void ifListIsEmptyThenException(List<Department> departments) {
        if (departments.isEmpty()) {
            log.error("Fail when searching for departments list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }
}


