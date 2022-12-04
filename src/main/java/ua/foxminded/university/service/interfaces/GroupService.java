package ua.foxminded.university.service.interfaces;

import ua.foxminded.university.domain.Group;

import java.util.List;

public interface GroupService extends GenericService<Group> {

    List<Group> findByDepartment(int id);

    List<Group> findBySubject(int id);

    List<Group> findByLecture(int id);
}
