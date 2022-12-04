package ua.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.domain.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByDepartmentId(int id);

    @Query("SELECT e FROM Employee e WHERE e.id NOT IN (SELECT t.employee.id FROM Teacher t)")
    List<Employee> findNotTeachers();
}
