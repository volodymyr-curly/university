package ua.foxminded.university.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;

@Component
public class LectureDTOConverter implements Converter<String, LectureResponseDTO> {

    @Override
    public LectureResponseDTO convert(@NonNull String id) {
        return LectureResponseDTO.builder().id(Integer.parseInt(id)).build();
    }
}
