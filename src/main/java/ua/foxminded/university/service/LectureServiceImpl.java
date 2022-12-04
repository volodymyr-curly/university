package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.domain.*;
import ua.foxminded.university.repository.LectureRepository;
import ua.foxminded.university.service.interfaces.LectureService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class LectureServiceImpl implements LectureService {

    private static final String NOT_FOUND_MESSAGE = format(ENTITY_NOT_FOUND, Lecture.class.getSimpleName());
    private static final String EXISTS_MESSAGE = format(ENTITY_EXISTS, Lecture.class.getSimpleName());

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    @Transactional
    public void add(Lecture lecture) {
        ifAlreadyExistThenException(lecture);
        lectureRepository.save(lecture);
    }

    @Override
    @Transactional
    public void update(int id, Lecture updatedLecture) {
        ifNotFoundThenException(id);
        ifAlreadyExistThenException(updatedLecture);
        lectureRepository.save(updatedLecture);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        lectureRepository.deleteById(id);
    }

    @Override
    public Lecture find(int id) {
        return lectureRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Failed to find lecture with id={}", id);
                return new EntityNotFoundException(NOT_FOUND_MESSAGE);
            });
    }

    @Override
    public List<Lecture> findAll() {
        List<Lecture> lectures = lectureRepository.findAll();
        ifListIsEmptyThenException(lectures);
        return lectures;
    }

    @Override
    public List<Lecture> findBySubject(int id) {
        List<Lecture> lectures = lectureRepository.findBySubjectId(id);
        ifListIsEmptyThenException(lectures);
        return lectures;
    }

    @Override
    public List<Lecture> findByTeacher(int id) {
        List<Lecture> lectures = lectureRepository.findByTeacherId(id);
        ifListIsEmptyThenException(lectures);
        return lectures;
    }

    @Override
    public List<Lecture> findByGroup(int id) {
        List<Lecture> lectures = lectureRepository.findByGroupsId(id);
        ifListIsEmptyThenException(lectures);
        return lectures;
    }

    @Override
    public List<Lecture> findByRoom(int id) {
        List<Lecture> lectures = lectureRepository.findByRoomId(id);
        ifListIsEmptyThenException(lectures);
        return lectures;
    }

    @Override
    public List<Lecture> findByDuration(int id) {
        List<Lecture> lectures = lectureRepository.findByDurationId(id);
        ifListIsEmptyThenException(lectures);
        return lectures;
    }

    private void ifNotFoundThenException(int id) {
        if (!lectureRepository.existsById(id)) {
            log.error("Lecture with id={} not found", id);
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private void ifListIsEmptyThenException(List<Lecture> lectures) {
        if (lectures.isEmpty()) {
            log.error("Fail when searching for lectures list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }

    private void ifAlreadyExistThenException(Lecture lecture) {
        if (lecture.getId() == 0) {

            if (exist(lecture)) {
                log.error("Exists {}", lecture);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }

        } else {
            Lecture lectureToUpdate = lectureRepository.findById(lecture.getId()).orElseThrow(() -> {
                log.error("Failed to find lecture with id={}", lecture.getId());
                return new EntityNotFoundException(NOT_FOUND_MESSAGE);
            });

            boolean equalLectures = compareLectures(lectureToUpdate, lecture);

            if (!equalLectures && exist(lecture)) {
                log.error("Exists {}", lecture);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }
        }
    }

    private boolean exist(Lecture lecture) {
        return lectureRepository.existsSameLecture(lecture.getDate(),
            lecture.getSubject().getId(),
            lecture.getTeacher().getId(),
            lecture.getRoom().getId(),
            lecture.getDuration().getId());
    }

    private boolean compareLectures(Lecture updatedLecture, Lecture lectureToUpdate) {
        return
            lectureToUpdate.getDate().equals(updatedLecture.getDate()) &&
                updatedLecture.getSubject().getId() == lectureToUpdate.getSubject().getId() &&
                updatedLecture.getTeacher().getId() == lectureToUpdate.getTeacher().getId() &&
                updatedLecture.getRoom().getId() == lectureToUpdate.getRoom().getId() &&
                updatedLecture.getDuration().getId() == lectureToUpdate.getDuration().getId();
    }
}
