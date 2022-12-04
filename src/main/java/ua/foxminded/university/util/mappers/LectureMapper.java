package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Lecture;
import ua.foxminded.university.dto.lecture.LectureRequestDTO;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.service.interfaces.LectureService;

import java.util.List;

@Component
public class LectureMapper {

    private final LectureService lectureService;

    @Autowired
    public LectureMapper(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public Lecture convertToLecture(LectureRequestDTO lectureDTO) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper.map(lectureDTO, Lecture.class);
    }

    public LectureResponseDTO convertToLectureResponseDTO(Lecture lecture) {
        return new ModelMapper().map(lecture, LectureResponseDTO.class);
    }

    public LectureRequestDTO convertToLectureRequestDTO(Lecture lecture) {
        return new ModelMapper().map(lecture, LectureRequestDTO.class);
    }

    public List<LectureResponseDTO> getLecturesDTO() {
        return lectureService.findAll().stream()
            .map(this::convertToLectureResponseDTO)
            .toList();
    }
}
