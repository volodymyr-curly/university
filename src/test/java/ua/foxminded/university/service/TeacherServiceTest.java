package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Teacher;
import ua.foxminded.university.repository.TeacherRepository;
import ua.foxminded.university.service.interfaces.TeacherService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(SpringExtension.class)
@Import(TeacherServiceImpl.class)
class TeacherServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int SINGLE_INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Teacher EXPECTED_TEACHER = setExpectedTeachers().get(0);

    @Autowired
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    void givenTeacher_whenAddTeacher_thenTeacher() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(EXPECTED_TEACHER);

        teacherService.add(EXPECTED_TEACHER);

        verify(teacherRepository, times(SINGLE_INVOCATION_NUMBER)).save(any(Teacher.class));
    }

    @Test
    void givenTeacher_whenUpdateTeacher_thenUpdatedTeacher() {
        when(teacherRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_TEACHER));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(EXPECTED_TEACHER);

        teacherService.update(ACTUAL_ID, EXPECTED_TEACHER);

        verify(teacherRepository, times(SINGLE_INVOCATION_NUMBER)).findById(ACTUAL_ID);
        verify(teacherRepository, times(SINGLE_INVOCATION_NUMBER)).save(any(Teacher.class));
    }

    @Test
    void givenTeacher_whenDeleteTeacher_thenNoTeacher() {
        doNothing().when(teacherRepository).deleteById(ACTUAL_ID);
        when(teacherRepository.existsById(ACTUAL_ID)).thenReturn(true);
        teacherService.delete(ACTUAL_ID);

        verify(teacherRepository, times(SINGLE_INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenTeacherId_whenFindTeacher_thenTeacher() {
        when(teacherRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_TEACHER));

        Teacher actualTeacher = teacherService.find(ACTUAL_ID);

        assertEquals(setInitialTeachers().get(0), actualTeacher);
        verify(teacherRepository, times(SINGLE_INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindTeachers_thenListOfTeachers() {
        when(teacherRepository.findAll()).thenReturn(setExpectedTeachers());

        List<Teacher> actualTeachers = teacherService.findAll();

        assertEquals(setExpectedTeachers(), actualTeachers);
        verify(teacherRepository, times(SINGLE_INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenSubjectId_whenFindTeacherBySubject_thenListOfTeacher() {
        when(teacherRepository.findBySubjectsId(ACTUAL_ID)).thenReturn(setExpectedTeachers());

        List<Teacher> actualTeachers = teacherService.findBySubject(ACTUAL_ID);

        assertEquals(setExpectedTeachers(), actualTeachers);
        verify(teacherRepository, times(SINGLE_INVOCATION_NUMBER)).findBySubjectsId(ACTUAL_ID);
    }

    @Test
    void givenDepartmentId_whenFindTeacherBySubject_thenListOfTeacher() {
        when(teacherRepository.findByDepartmentId(ACTUAL_ID)).thenReturn(setExpectedTeachers());

        List<Teacher> actualTeachers = teacherService.findByDepartment(ACTUAL_ID);

        assertEquals(setExpectedTeachers(), actualTeachers);
        verify(teacherRepository, times(SINGLE_INVOCATION_NUMBER)).findByDepartmentId(ACTUAL_ID);
    }

    @Test
    void givenTeacher_whenUpdateTeacher_thenEntityNotFoundException() {
        when(teacherRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE));

        assertThrows(EntityNotFoundException.class, () -> teacherService.update(ACTUAL_ID, EXPECTED_TEACHER));
    }

    @Test
    void givenTeacher_whenDeleteTeacher_thenEntityNotFoundException() {
        when(teacherRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> teacherService.delete(ACTUAL_ID));
    }

    @Test
    void givenTeacherId_whenFindTeacher_thenEntityNotFoundException() {
        when(teacherRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE));

        assertThrows(EntityNotFoundException.class, () -> teacherService.find(ACTUAL_ID));
    }

    @Test
    void whenFindTeachers_thenEntityNotFoundException() {
        when(teacherRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> teacherService.findAll());
    }

    @Test
    void givenSubjectId_whenFindTeachersById_thenEntityNotFoundException() {
        when(teacherRepository.findBySubjectsId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> teacherService.findBySubject(ACTUAL_ID));
    }

    @Test
    void givenDepartmentId_whenFindTeachersById_thenEntityNotFoundException() {
        when(teacherRepository.findByDepartmentId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> teacherService.findByDepartment(ACTUAL_ID));
    }
}
