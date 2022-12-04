package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.dto.subject.SubjectRequestDTO;
import ua.foxminded.university.dto.subject.SubjectResponseDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;
import ua.foxminded.university.service.interfaces.SubjectService;

import java.util.List;

@Component
public class SubjectMapper {

    private final SubjectService subjectService;

    @Autowired
    public SubjectMapper(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public Subject convertToSubject(SubjectRequestDTO subjectDTO) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper.map(subjectDTO, Subject.class);
    }

    public SubjectRequestDTO convertToSubjectRequestDTO(Subject subject) {
        return new ModelMapper().map(subject, SubjectRequestDTO.class);
    }

    public SubjectResponseDTO convertToSubjectResponseDTO(Subject subject) {
        return new ModelMapper().map(subject, SubjectResponseDTO.class);
    }

    public SubjectResponseDTO convertToSubjectResponseDTOWhenFindAll(Subject subject) {
        PropertyMap<Subject, SubjectResponseDTO> FindOne = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getLectures());
                skip(destination.getTeachers());
            }
        };

        return new ModelMapper().addMappings(FindOne).map(subject);
    }

    public SubjectNestedDTO convertToSubjectNestedDTO(Subject subject) {
        return new ModelMapper().map(subject, SubjectNestedDTO.class);
    }

    public List<SubjectNestedDTO> getSubjectsDTO() {
        return subjectService.findAll().stream()
            .map(this::convertToSubjectNestedDTO)
            .toList();
    }
}
