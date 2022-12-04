package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.repository.SubjectRepository;
import ua.foxminded.university.domain.*;
import ua.foxminded.university.service.interfaces.SubjectService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    private static final String NOT_FOUND_MESSAGE = format(ENTITY_NOT_FOUND, Subject.class.getSimpleName());
    private static final String EXISTS_MESSAGE = format(ENTITY_EXISTS, Subject.class.getSimpleName());

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional
    public void add(Subject subject) {
        ifAlreadyExistThenException(subject);
        subjectRepository.save(subject);
    }

    @Override
    @Transactional
    public void update(int id, Subject updatedSubject) {
        ifNotFoundThenException(id);
        ifAlreadyExistThenException(updatedSubject);
        subjectRepository.save(updatedSubject);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        subjectRepository.deleteById(id);
    }

    @Override
    public Subject find(int id) {
        return subjectRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to find subject with id={}", id);
            return new EntityNotFoundException(NOT_FOUND_MESSAGE);
        });
    }

    @Override
    public List<Subject> findAll() {
        List<Subject> subjects = subjectRepository.findAll();
        ifListIsEmptyThenException(subjects);
        return subjects;
    }

    @Override
    public List<Subject> findByGroup(int id) {
        List<Subject> subjects = subjectRepository.findByGroupsId(id);
        ifListIsEmptyThenException(subjects);
        return subjects;
    }

    @Override
    public List<Subject> findByTeacher(int id) {
        List<Subject> subjects = subjectRepository.findByTeachersId(id);
        ifListIsEmptyThenException(subjects);
        return subjects;
    }

    private void ifAlreadyExistThenException(Subject subject) {
        if (subject.getId() == 0) {
            if (subjectRepository.findAll().contains(subject)) {
                log.error("Exists {}", subject);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }

        } else {
            if ((!subjectRepository.findById(subject.getId()).get().equals(subject)) && (subjectRepository.findAll().contains(subject))) {
                log.error("Exists {}", subject);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }
        }
    }

    private void ifNotFoundThenException(int id) {
        if (!subjectRepository.existsById(id)) {
            log.error("Subject with id={} not found", id);
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private void ifListIsEmptyThenException(List<Subject> subjects) {
        if (subjects.isEmpty()) {
            log.error("Fail when searching for subjects list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }
}
