package ua.foxminded.university.service.interfaces;

import ua.foxminded.university.domain.Student;

import java.util.List;

public interface StudentService extends GenericService<Student> {

    List<Student> findByGroup(int id);
}
