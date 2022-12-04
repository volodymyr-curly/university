package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.repository.AddressRepository;
import ua.foxminded.university.repository.PersonRepository;
import ua.foxminded.university.repository.StudentRepository;
import ua.foxminded.university.service.interfaces.StudentService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(SpringExtension.class)
@Import(StudentServiceImpl.class)
class StudentServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Student EXPECTED_STUDENT = setExpectedStudents().get(0);

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void givenStudent_whenAddStudent_thenStudent() {
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(EXPECTED_STUDENT.getPassword())).thenReturn(EXPECTED_STUDENT.getPassword());
        when(studentRepository.save(any(Student.class))).thenReturn(EXPECTED_STUDENT);
        EXPECTED_STUDENT.setId(0);

        studentService.add(EXPECTED_STUDENT);

        verify(studentRepository, times(INVOCATION_NUMBER)).save(EXPECTED_STUDENT);
        verify(personRepository, times(INVOCATION_NUMBER)).findAll();
        verify(passwordEncoder, times(INVOCATION_NUMBER)).encode(EXPECTED_STUDENT.getPassword());
    }

    @Test
    void givenStudent_whenUpdateStudent_thenUpdatedStudent() {
        when(addressRepository.findByPersonId(ACTUAL_ID)).thenReturn(setExpectedAddresses().get(0));
        when(studentRepository.save(any(Student.class))).thenReturn(EXPECTED_STUDENT);
        when(studentRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(personRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_STUDENT));
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(EXPECTED_STUDENT.getPassword())).thenReturn(EXPECTED_STUDENT.getPassword());

        studentService.update(ACTUAL_ID, EXPECTED_STUDENT);

        verify(addressRepository, times(INVOCATION_NUMBER)).findByPersonId(ACTUAL_ID);
        verify(studentRepository, times(INVOCATION_NUMBER)).save(any(Student.class));
        verify(studentRepository, times(INVOCATION_NUMBER)).existsById(ACTUAL_ID);
        verify(passwordEncoder, times(INVOCATION_NUMBER)).encode(EXPECTED_STUDENT.getPassword());
    }

    @Test
    void givenStudent_whenDeleteStudent_thenNoStudent() {
        doNothing().when(studentRepository).deleteById(ACTUAL_ID);
        when(studentRepository.existsById(ACTUAL_ID)).thenReturn(true);

        studentService.delete(ACTUAL_ID);

        verify(studentRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenStudentId_whenFindStudent_thenStudent() {
        when(studentRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_STUDENT));

        Student actualStudent = studentService.find(ACTUAL_ID);

        assertEquals(EXPECTED_STUDENT, actualStudent);
        verify(studentRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindStudents_thenListOfStudents() {
        when(studentRepository.findAll()).thenReturn(setExpectedStudents());

        List<Student> actualStudents = studentService.findAll();

        assertEquals(setExpectedStudents(), actualStudents);
        verify(studentRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenGroupId_whenFindStudentByGroup_thenListOfStudent() {
        when(studentRepository.findByGroupId(ACTUAL_ID)).thenReturn(setExpectedStudents());

        List<Student> actualStudents = studentService.findByGroup(ACTUAL_ID);

        assertEquals(setExpectedStudents(), actualStudents);
        verify(studentRepository, times(INVOCATION_NUMBER)).findByGroupId(ACTUAL_ID);
    }

    @Test
    void givenStudent_whenAddStudent_thenEntityExistsException() {
        EXPECTED_STUDENT.setId(0);
        when(personRepository.findAll()).thenReturn(Collections.singletonList(EXPECTED_STUDENT));

        assertThrows(EntityExistsException.class, () -> studentService.add(EXPECTED_STUDENT));
    }


    @Test
    void givenStudent_whenUpdateStudent_thenEntityExistsException() {
        when(studentRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(personRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(setExpectedStudents().get(1)));
        when(personRepository.findAll()).thenReturn(Collections.singletonList(EXPECTED_STUDENT));

        assertThrows(EntityExistsException.class, () -> studentService.update(ACTUAL_ID, EXPECTED_STUDENT));
    }

    @Test
    void givenStudent_whenUpdateStudent_thenEntityNotFoundException() {
        when(studentRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> studentService.update(ACTUAL_ID, EXPECTED_STUDENT));
    }

    @Test
    void givenStudent_whenDeleteStudent_thenEntityNotFoundException() {
        when(studentRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> studentService.delete(ACTUAL_ID));
    }

    @Test
    void givenStudentId_whenFindStudent_thenEntityNotFoundException() {
        when(studentRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> studentService.find(ACTUAL_ID));
    }

    @Test
    void whenFindStudents_thenEntityNotFoundException() {
        when(studentRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> studentService.findAll());
    }

    @Test
    void givenGroupId_whenFindStudentsById_thenEntityNotFoundException() {
        when(studentRepository.findByGroupId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> studentService.findByGroup(ACTUAL_ID));
    }
}
