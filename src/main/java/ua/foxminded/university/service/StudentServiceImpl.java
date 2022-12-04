package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.domain.Address;
import ua.foxminded.university.domain.Person;
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.repository.AddressRepository;
import ua.foxminded.university.repository.PersonRepository;
import ua.foxminded.university.repository.StudentRepository;
import ua.foxminded.university.service.interfaces.StudentService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static ua.foxminded.university.domain.Role.ROLE_STUDENT;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class StudentServiceImpl implements StudentService {

    private static final String NOT_FOUND_MESSAGE = format(ENTITY_NOT_FOUND, Person.class.getSimpleName());
    private static final String EXISTS_MESSAGE = format(ENTITY_EXISTS, Person.class.getSimpleName());

    private final StudentRepository studentRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              PersonRepository personRepository,
                              AddressRepository addressRepository,
                              PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void add(Student student) {
        ifAlreadyExistThenException(student);
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        if (Objects.isNull(student.getRole())) {
            student.setRole(ROLE_STUDENT);
        }

        studentRepository.save(student);
        student.getAddress().setPerson(student);
    }

    @Override
    @Transactional
    public void update(int id, Student updatedStudent) {
        ifNotFoundThenException(id);
        ifAlreadyExistThenException(updatedStudent);
        Address addressToUpdate = addressRepository.findByPersonId(id);
        updatedStudent.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
        updatedStudent.getAddress().setId(addressToUpdate.getId());
        updatedStudent.getAddress().setPerson(updatedStudent);
        studentRepository.save(updatedStudent);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        studentRepository.deleteById(id);
    }

    @Override
    public Student find(int id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Failed to find student with id={}", id);
                return new EntityNotFoundException(NOT_FOUND_MESSAGE);
            });
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = studentRepository.findAll();
        ifListIsEmptyThenException(students);
        return students;
    }

    @Override
    public List<Student> findByGroup(int id) {
        List<Student> students = studentRepository.findByGroupId(id);
        ifListIsEmptyThenException(students);
        return students;
    }

    private void ifAlreadyExistThenException(Student student) {
        if (student.getId() == 0) {
            if (personRepository.findAll().contains(student)) {
                log.error("Exists {}", student);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }

        } else {
            if ((!personRepository.findById(student.getId()).get().equals(student))
                && (personRepository.findAll().contains(student))) {
                log.error("Exists {}", student);
                throw new EntityExistsException(EXISTS_MESSAGE);
            }
        }
    }

    private void ifNotFoundThenException(int id) {
        if (!studentRepository.existsById(id)) {
            log.error("Student with id={} not found", id);
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private void ifListIsEmptyThenException(List<Student> students) {
        if (students.isEmpty()) {
            log.error("Fail when searching for students list");
            throw new EntityNotFoundException(NOT_FOUND);
        }
    }
}
