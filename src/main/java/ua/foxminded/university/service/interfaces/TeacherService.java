package ua.foxminded.university.service.interfaces;

import ua.foxminded.university.domain.Teacher;

import java.util.List;

public interface TeacherService extends GenericService<Teacher> {

    List<Teacher> findBySubject(int id);

    List<Teacher> findByDepartment(int id);
}
