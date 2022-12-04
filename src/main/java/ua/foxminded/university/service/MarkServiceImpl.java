package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.repository.MarkRepository;
import ua.foxminded.university.domain.Mark;
import ua.foxminded.university.service.interfaces.MarkService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class MarkServiceImpl implements MarkService {

    private static final String NOT_FOUND_MESSAGE = format(ENTITY_NOT_FOUND, Mark.class.getSimpleName());
    private static final String EXISTS_MESSAGE = format(ENTITY_EXISTS, Mark.class.getSimpleName());

    private final MarkRepository markRepository;

    @Autowired
    public MarkServiceImpl(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    @Override
    @Transactional
    public void add(Mark mark) {
        ifAlreadyExistThenException(mark);
        markRepository.save(mark);
    }

    @Override
    @Transactional
    public void update(int id, Mark updatedMark) {
        ifNotFoundThenException(id);
        ifAlreadyExistThenException(updatedMark);
        markRepository.save(updatedMark);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        markRepository.deleteById(id);
    }

    @Override
    public Mark find(int id) {
        return markRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Failed to find mark with id={}", id);
                return new EntityNotFoundException(NOT_FOUND_MESSAGE);
            });
    }

    @Override
    public List<Mark> findAll() {
        List<Mark> marks = markRepository.findAll();
        ifListIsEmptyThenException(marks);
        return marks;
    }

    @Override
    public List<Mark> findByStudent(int id) {
        List<Mark> marks = markRepository.findByStudentId(id);
        ifListIsEmptyThenException(marks);
        return marks;
    }

    @Override
    public List<Mark> findBySubject(int id) {
        List<Mark> marks = markRepository.findBySubjectId(id);
        ifListIsEmptyThenException(marks);
        return marks;
    }

    private void ifNotFoundThenException(int id) {
        if (!markRepository.existsById(id)) {
            log.error("Mark with id={} not found", id);
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private void ifListIsEmptyThenException(List<Mark> marks) {
        if (marks.isEmpty()) {
            log.error("Fail when searching for marks list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }

    private void ifAlreadyExistThenException(Mark mark) {
        if (mark.getId() == 0) {

            if (exist(mark)) {
                log.error("Exists {}", mark);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }

        } else {
            Mark markToUpdate = markRepository.findById(mark.getId()).get();
            boolean equalMarks = compareMarks(mark, markToUpdate);

            if (!equalMarks && markRepository.existsSameMark(mark.getValue(),
                mark.getStudent().getId(), mark.getSubject().getId())) {
                log.error("Exists {}", mark);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }
        }
    }

    private boolean exist(Mark mark) {
        return markRepository.existsSameMark(mark.getValue(),
            mark.getStudent().getId(),
            mark.getSubject().getId());
    }

    private boolean compareMarks(Mark updatedMark, Mark markToUpdate) {
        return
            markToUpdate.getValue().equals(updatedMark.getValue()) &&
                markToUpdate.getStudent().getId() == updatedMark.getStudent().getId() &&
                markToUpdate.getSubject().getId() == updatedMark.getSubject().getId();
    }
}
