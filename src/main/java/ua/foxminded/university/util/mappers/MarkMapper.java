package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Mark;
import ua.foxminded.university.dto.mark.MarkRequestDTO;
import ua.foxminded.university.dto.mark.MarkResponseDTO;

@Component
public class MarkMapper {

    public Mark convertToMark(MarkRequestDTO markDTO) {
        return new ModelMapper().map(markDTO, Mark.class);
    }

    public MarkResponseDTO convertToMarkResponseDTO(Mark mark) {
        return new ModelMapper().map(mark, MarkResponseDTO.class);
    }

    public MarkRequestDTO convertToMarkRequestDTO(Mark mark) {
        return new ModelMapper().map(mark, MarkRequestDTO.class);
    }
}
