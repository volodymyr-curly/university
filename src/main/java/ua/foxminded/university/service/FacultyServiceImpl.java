package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.repository.FacultyRepository;
import ua.foxminded.university.domain.Faculty;
import ua.foxminded.university.service.interfaces.FacultyService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class FacultyServiceImpl implements FacultyService {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, Faculty.class.getSimpleName());

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    @Transactional
    public void add(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    @Override
    @Transactional
    public void update(int id, Faculty updatedFaculty) {
        ifNotFoundThenException(id);
        facultyRepository.save(updatedFaculty);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty find(int id) {
        return facultyRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to find faculty with id={}", id);
            return new EntityNotFoundException(MESSAGE);
        });
    }

    @Override
    public List<Faculty> findAll() {
        List<Faculty> faculties = facultyRepository.findAll();
        ifListIsEmptyThenException(faculties);
        return faculties;
    }

    private void ifNotFoundThenException(int id) {
        if (!facultyRepository.existsById(id)) {
            log.error("Faculty with id={} not found", id);
            throw new EntityNotFoundException(MESSAGE);
        }
    }

    private void ifListIsEmptyThenException(List<Faculty> faculties) {
        if (faculties.isEmpty()) {
            log.error("Fail when searching for faculties list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }
}
