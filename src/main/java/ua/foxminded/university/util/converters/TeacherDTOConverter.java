package ua.foxminded.university.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.foxminded.university.dto.teacher.TeacherNestedDTO;

@Component
public class TeacherDTOConverter implements Converter<String, TeacherNestedDTO> {

    @Override
    public TeacherNestedDTO convert(@NonNull String id) {
        return TeacherNestedDTO.builder().id(Integer.parseInt(id)).build();
    }
}
