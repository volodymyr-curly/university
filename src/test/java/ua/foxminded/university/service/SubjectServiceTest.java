package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.repository.SubjectRepository;
import ua.foxminded.university.service.interfaces.SubjectService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedSubjects;

@ExtendWith(SpringExtension.class)
@Import(SubjectServiceImpl.class)
class SubjectServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Subject EXPECTED_SUBJECT = setExpectedSubjects().get(0);

    @Autowired
    private SubjectService subjectService;

    @MockBean
    private SubjectRepository subjectRepository;

    @Test
    void givenSubject_whenAddSubject_thenSubject() {
        when(subjectRepository.findAll()).thenReturn(new ArrayList<>());
        when(subjectRepository.save(any(Subject.class))).thenReturn(EXPECTED_SUBJECT);
        EXPECTED_SUBJECT.setId(0);

        subjectService.add(EXPECTED_SUBJECT);

        verify(subjectRepository, times(INVOCATION_NUMBER)).findAll();
        verify(subjectRepository, times(INVOCATION_NUMBER)).save(any(Subject.class));
    }

    @Test
    void givenSubject_whenUpdateSubject_thenUpdatedSubject() {
        when(subjectRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(subjectRepository.findAll()).thenReturn(new ArrayList<>());
        when(subjectRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_SUBJECT));
        when(subjectRepository.save(any(Subject.class))).thenReturn(EXPECTED_SUBJECT);

        subjectService.update(ACTUAL_ID, EXPECTED_SUBJECT);

        verify(subjectRepository, times(INVOCATION_NUMBER)).save(any(Subject.class));
        verify(subjectRepository, times(INVOCATION_NUMBER)).existsById(ACTUAL_ID);
        verify(subjectRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenSubject_whenDeleteSubject_thenNoSubject() {
        doNothing().when(subjectRepository).deleteById(ACTUAL_ID);
        when(subjectRepository.existsById(ACTUAL_ID)).thenReturn(true);

        subjectService.delete(ACTUAL_ID);

        verify(subjectRepository, times(INVOCATION_NUMBER)).existsById(ACTUAL_ID);
        verify(subjectRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenSubjectId_whenFindSubject_thenSubject() {
        when(subjectRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_SUBJECT));

        Subject actualSubject = subjectService.find(ACTUAL_ID);

        assertEquals(EXPECTED_SUBJECT, actualSubject);
        verify(subjectRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindSubjects_thenListOfSubjects() {
        when(subjectRepository.findAll()).thenReturn(setExpectedSubjects());

        List<Subject> actualSubjects = subjectService.findAll();

        assertEquals(setExpectedSubjects(), actualSubjects);
        verify(subjectRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenTeacherId_whenFindSubjectsByTeacher_thenListOfSubjects() {
        when(subjectRepository.findByTeachersId(ACTUAL_ID)).thenReturn(setExpectedSubjects());

        List<Subject> actualSubjects = subjectService.findByTeacher(ACTUAL_ID);

        assertEquals(setExpectedSubjects(), actualSubjects);
        verify(subjectRepository, times(INVOCATION_NUMBER)).findByTeachersId(ACTUAL_ID);
    }

    @Test
    void givenGroupId_whenFindSubjectsByGroup_thenListOfSubjects() {
        when(subjectRepository.findByGroupsId(ACTUAL_ID)).thenReturn(setExpectedSubjects());

        List<Subject> actualSubjects = subjectService.findByGroup(ACTUAL_ID);

        assertEquals(setExpectedSubjects(), actualSubjects);
        verify(subjectRepository, times(INVOCATION_NUMBER)).findByGroupsId(ACTUAL_ID);
    }

    @Test
    void givenSubject_whenAddSubject_thenEntityExistsException() {
        EXPECTED_SUBJECT.setId(0);
        when(subjectRepository.findAll()).thenReturn(setExpectedSubjects());

        assertThrows(EntityExistsException.class, () -> subjectService.add(EXPECTED_SUBJECT));
    }

    @Test
    void givenSubject_whenUpdateSubject_thenEntityNotFoundException() {
        when(subjectRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> subjectService.update(ACTUAL_ID, EXPECTED_SUBJECT));
    }

    @Test
    void givenSubject_whenUpdateSubject_thenEntityExistsException() {
        when(subjectRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(subjectRepository.findAll()).thenReturn(setExpectedSubjects());
        when(subjectRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(setExpectedSubjects().get(1)));

        assertThrows(EntityExistsException.class, () -> subjectService.update(ACTUAL_ID, EXPECTED_SUBJECT));
    }

    @Test
    void givenSubject_whenDeleteSubject_thenEntityNotFoundException() {
        when(subjectRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> subjectService.delete(ACTUAL_ID));
    }

    @Test
    void givenSubjectId_whenFindSubject_thenEntityNotFoundException() {
        when(subjectRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> subjectService.find(ACTUAL_ID));
    }

    @Test
    void whenFindSubjects_thenEntityNotFoundException() {
        when(subjectRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> subjectService.findAll());
    }

    @Test
    void givenTeacherId_whenFindSubjectsById_thenEntityNotFoundException() {
        when(subjectRepository.findByTeachersId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> subjectService.findByTeacher(ACTUAL_ID));
    }

    @Test
    void givenGroupId_whenFindSubjectsById_thenEntityNotFoundException() {
        when(subjectRepository.findByGroupsId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> subjectService.findByGroup(ACTUAL_ID));
    }
}
