package ua.foxminded.university.service.interfaces;

import ua.foxminded.university.domain.Lecture;

import java.util.List;

public interface LectureService extends GenericService<Lecture> {

    List<Lecture> findBySubject(int id);

    List<Lecture> findByTeacher(int id);

    List<Lecture> findByRoom(int id);

    List<Lecture> findByDuration(int id);

    List<Lecture> findByGroup(int id);
}
