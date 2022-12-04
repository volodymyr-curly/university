package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.dto.student.StudentNestedDTO;
import ua.foxminded.university.dto.student.StudentRequestDTO;
import ua.foxminded.university.dto.student.StudentResponseDTO;
import ua.foxminded.university.service.interfaces.StudentService;

import java.util.List;

@Component
public class StudentMapper {

    private final StudentService studentService;

    @Autowired
    public StudentMapper(StudentService studentService) {
        this.studentService = studentService;
    }

    public StudentResponseDTO convertToDTOWhenFindOne(Student student) {
        PropertyMap<Student, StudentResponseDTO> FindOne = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
            }
        };

        return new ModelMapper().addMappings(FindOne).map(student);
    }

    public StudentResponseDTO convertToDTOWhenFindAll(Student student) {
        return StudentResponseDTO.builder()
            .id(student.getId())
            .firstName(student.getFirstName())
            .lastName(student.getLastName())
            .groupName(student.getGroup().getName())
            .build();

    }

    public StudentRequestDTO convertToStudentRequestDTO(Student student) {
        return new ModelMapper().map(student, StudentRequestDTO.class);
    }

    public Student convertToStudent(StudentRequestDTO studentDTO) {
        return new ModelMapper().map(studentDTO, Student.class);
    }

    public StudentNestedDTO convertToStudentNestedDTO(Student student) {
        return new ModelMapper().map(student, StudentNestedDTO.class);
    }

    public List<StudentNestedDTO> getStudentDTO() {
        return studentService.findAll().stream()
            .map(this::convertToStudentNestedDTO)
            .toList();
    }
}
