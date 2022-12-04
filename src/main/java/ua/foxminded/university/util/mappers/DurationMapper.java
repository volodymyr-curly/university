package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Duration;
import ua.foxminded.university.dto.duration.DurationResponseDTO;
import ua.foxminded.university.dto.duration.DurationRequestDTO;
import ua.foxminded.university.service.interfaces.DurationService;

import java.util.List;

@Component
public class DurationMapper {

    private final DurationService durationService;

    @Autowired
    public DurationMapper(DurationService durationService) {
        this.durationService = durationService;
    }

    public Duration convertToDuration(DurationRequestDTO durationDTO) {
        return new ModelMapper().map(durationDTO, Duration.class);
    }

    public DurationResponseDTO convertToDurationResponseDTO(Duration duration) {
        return new ModelMapper().map(duration, DurationResponseDTO.class);
    }

    public DurationRequestDTO convertToDurationRequestDTO(Duration duration) {
        return new ModelMapper().map(duration, DurationRequestDTO.class);
    }

    public List<DurationRequestDTO> getDurationsDTO() {
        return durationService.findAll().stream()
            .map(this::convertToDurationRequestDTO)
            .toList();
    }
}
