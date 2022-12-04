package ua.foxminded.university.service.interfaces;

import ua.foxminded.university.domain.Subject;

import java.util.List;

public interface SubjectService extends GenericService<Subject> {

    List<Subject> findByGroup(int id);

    List<Subject> findByTeacher(int id);
}
