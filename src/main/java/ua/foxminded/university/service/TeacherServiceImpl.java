package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.domain.*;
import ua.foxminded.university.repository.TeacherRepository;
import ua.foxminded.university.service.interfaces.TeacherService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Teacher.class.getSimpleName());

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    @Transactional
    public void add(Teacher teacher) {
        teacherRepository.save(teacher);
        teacher.getEmployee().setTeacher(teacher);
        teacher.getSubjects().forEach(subject -> subject.addTeacher(teacher));
    }

    @Override
    @Transactional
    public void update(int id, Teacher updatedTeacher) {
        Teacher teacherToUpdate = findTeacher(id);
        updatedTeacher.setEmployee(teacherToUpdate.getEmployee());
        teacherToUpdate.merge(updatedTeacher);
        teacherRepository.save(updatedTeacher);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (!teacherRepository.existsById(id)) {
            log.error("Teacher with id={} not found", id);
            throw new EntityNotFoundException(MESSAGE);
        }

        teacherRepository.deleteById(id);
    }

    @Override
    public Teacher find(int id) {
        return findTeacher(id);
    }

    @Override
    public List<Teacher> findAll() {
        List<Teacher> teachers = teacherRepository.findAll();
        ifListIsEmptyThenException(teachers);
        return teachers;
    }

    @Override
    public List<Teacher> findBySubject(int id) {
        List<Teacher> teachers = teacherRepository.findBySubjectsId(id);
        ifListIsEmptyThenException(teachers);
        return teachers;
    }

    @Override
    public List<Teacher> findByDepartment(int id) {
        List<Teacher> teachers = teacherRepository.findByDepartmentId(id);
        ifListIsEmptyThenException(teachers);
        return teachers;
    }

    private Teacher findTeacher(int id) {
        return teacherRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to find teacher with id={}", id);
            return new EntityNotFoundException(MESSAGE);
        });
    }

    private void ifListIsEmptyThenException(List<Teacher> teachers) {
        if (teachers.isEmpty()) {
            log.error("Fail when searching for teachers list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }
}
