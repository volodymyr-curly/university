package ua.foxminded.university.service.interfaces;

import ua.foxminded.university.domain.Department;

import java.util.List;

public interface DepartmentService extends GenericService<Department> {

    List<Department> findByFaculty(int id);
}
