package ua.foxminded.university.util.mappers;

import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Teacher;
import ua.foxminded.university.dto.teacher.TeacherNestedDTO;
import ua.foxminded.university.dto.teacher.TeacherRequestDTO;
import ua.foxminded.university.dto.teacher.TeacherResponseDTO;
import ua.foxminded.university.service.interfaces.SubjectService;
import ua.foxminded.university.service.interfaces.TeacherService;

import java.util.List;

@Component
public class TeacherMapper {

    private final TeacherService teacherService;
    private final SubjectService subjectService;

    @Autowired
    public TeacherMapper(TeacherService teacherService, SubjectService subjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    public TeacherResponseDTO convertToTeacherResponseDTO(Teacher teacher) {
        PropertyMap<Teacher, TeacherResponseDTO> FindOne = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getEmployee().getId());
                skip(destination.getEmployee().getBirthDate());
                skip(destination.getEmployee().getPassword());
                skip(destination.getEmployee().getRole());
                skip(destination.getEmployee().getAddress());
                skip(destination.getEmployee().getEmploymentDate());
                skip(destination.getEmployee().getEmploymentType());
            }
        };
        return new ModelMapper().addMappings(FindOne).map(teacher);
    }

    public TeacherRequestDTO convertToTeacherDTO(Teacher teacher) {
        return new ModelMapper().map(teacher, TeacherRequestDTO.class);
    }

    public Teacher convertToTeacher(TeacherRequestDTO teacherDTO) {
        Teacher teacher = new ModelMapper().map(teacherDTO, Teacher.class);
        teacher.setSubjects(teacher.getSubjects()
            .stream()
            .map(subject -> subjectService.find(subject.getId()))
            .toList());
        return teacher;
    }

    public TeacherNestedDTO convertToTeacherNestedDTO(Teacher teacher) {
        return new ModelMapper().map(teacher, TeacherNestedDTO.class);
    }

    public List<TeacherNestedDTO> getTeachersDTO() {
        return teacherService.findAll().stream()
            .map(this::convertToTeacherNestedDTO)
            .toList();
    }
}
