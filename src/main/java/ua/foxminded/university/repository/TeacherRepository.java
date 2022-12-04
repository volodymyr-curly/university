package ua.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.domain.Teacher;
import ua.foxminded.university.dto.employee.EmployeeNestedDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Boolean existsByEmployeeId(int id);

    List<Teacher> findBySubjectsId(int id);

    @Query("SELECT t FROM Teacher t JOIN t.employee e WHERE e.department.id=:id")
    List<Teacher> findByDepartmentId(@Param("id") int id);
}
