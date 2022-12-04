package ua.foxminded.university.service.interfaces;

import ua.foxminded.university.domain.Mark;

import java.util.List;

public interface MarkService extends GenericService<Mark> {

    List<Mark> findByStudent(int studentId);

    List<Mark> findBySubject(int id);
}
