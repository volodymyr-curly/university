package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Faculty;
import ua.foxminded.university.dto.faculty.FacultyResponseDTO;
import ua.foxminded.university.dto.faculty.FacultyRequestDTO;
import ua.foxminded.university.service.interfaces.FacultyService;

import java.util.List;

@Component
public class FacultyMapper {

    private final FacultyService facultyService;

    @Autowired
    public FacultyMapper(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    public Faculty convertToFaculty(FacultyRequestDTO facultyDTO) {
        return new ModelMapper().map(facultyDTO, Faculty.class);
    }

    public FacultyResponseDTO convertToFacultyResponseDTO(Faculty faculty) {
        return new ModelMapper().map(faculty, FacultyResponseDTO.class);
    }

    public FacultyRequestDTO convertToFacultyRequestDTO(Faculty faculty) {
        return new ModelMapper().map(faculty, FacultyRequestDTO.class);
    }

    public List<FacultyResponseDTO> getFacultiesDTO() {
        return facultyService.findAll().stream()
            .map(this::convertToFacultyResponseDTO)
            .toList();
    }

    public List<FacultyRequestDTO> getFacultyDTO() {
        return facultyService.findAll().stream()
            .map(this::convertToFacultyRequestDTO)
            .toList();
    }
}
