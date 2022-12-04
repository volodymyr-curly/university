package ua.foxminded.university.service.interfaces;

import ua.foxminded.university.domain.Employee;

import java.util.List;

public interface EmployeeService extends GenericService<Employee> {

    List<Employee> findByDepartment(int id);

    List<Employee> findNotTeachers();
}
