package ua.foxminded.university.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;

@Component
public class SubjectDTOConverter implements Converter<String, SubjectNestedDTO> {

    @Override
    public SubjectNestedDTO convert(@NonNull String id) {
        return SubjectNestedDTO.builder().id(Integer.parseInt(id)).build();
    }
}
