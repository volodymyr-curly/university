package ua.foxminded.university.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.foxminded.university.dto.group.GroupNestedDTO;

@Component
public class GroupDTOConverter implements Converter<String, GroupNestedDTO> {

    @Override
    public GroupNestedDTO convert(@NonNull String id) {
        return GroupNestedDTO.builder().id(Integer.parseInt(id)).build();
    }
}
