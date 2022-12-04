package ua.foxminded.university.dto.duration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DurationResponseDTO {

    @Schema(example = "1")
    private Integer id;

    @Schema(example = "09:00")
    private String startTime;

    @Schema(example = "10:20")
    private String endTime;

    @ToString.Exclude
    private List<LectureResponseDTO> lectures;
}
