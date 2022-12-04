package ua.foxminded.university.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDTO {

    @Schema(example = "1")
    private Integer id;

    @Schema(example = "101")
    private Integer number;

    @Schema(example = "50")
    private Integer capacity;

    @ToString.Exclude
    private List<LectureResponseDTO> lectures;
}
