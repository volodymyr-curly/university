package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.domain.*;
import ua.foxminded.university.repository.GroupRepository;
import ua.foxminded.university.service.interfaces.GroupService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class GroupServiceImpl implements GroupService {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Group.class.getSimpleName());

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    @Transactional
    public void add(Group group) {
        groupRepository.save(group);
        group.getSubjects().forEach(subject -> subject.addGroup(group));
    }

    @Override
    @Transactional
    public void update(int id, Group updatedGroup) {
        Group groupToUpdate = findGroup(id);
        groupToUpdate.merge(updatedGroup);
        groupRepository.save(updatedGroup);
    }

    @Override
    @Transactional
    public void delete(int id) {

        if (!groupRepository.existsById(id)) {
            log.error("Group with id={} not found", id);
            throw new EntityNotFoundException(MESSAGE);
        }

        groupRepository.deleteById(id);
    }

    @Override
    public Group find(int id) {
        return findGroup(id);
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = groupRepository.findAll();
        ifListIsEmptyThenException(groups);
        return groups;
    }

    @Override
    public List<Group> findByDepartment(int id) {
        List<Group> groups = groupRepository.findByDepartmentId(id);
        ifListIsEmptyThenException(groups);
        return groups;
    }

    @Override
    public List<Group> findBySubject(int id) {
        List<Group> groups = groupRepository.findBySubjectsId(id);
        ifListIsEmptyThenException(groups);
        return groups;
    }

    @Override
    public List<Group> findByLecture(int id) {
        List<Group> groups = groupRepository.findByLecturesId(id);
        ifListIsEmptyThenException(groups);
        return groups;
    }

    private Group findGroup(int id) {
        return groupRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to find group with id={}", id);
            return new EntityNotFoundException(MESSAGE);
        });
    }

    private void ifListIsEmptyThenException(List<Group> groups) {
        if (groups.isEmpty()) {
            log.error("Fail when searching for groups list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }
}
