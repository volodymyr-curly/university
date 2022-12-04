package ua.foxminded.university.dto.mark;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.university.domain.MarkValue;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkResponseDTO {

    @Schema(example = "1")
    private Integer id;

    @Schema(enumAsRef = true)
    @Enumerated(EnumType.STRING)
    private MarkValue value;

    @Schema(example = "Economics And Law")
    private String subjectName;

    @Schema(example = "2022-01-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate subjectStartDate;

    @Schema(example = "Ivanenko")
    private String studentLastName;

    @Schema(example = "Ivan")
    private String studentFirstName;

    @Schema(example = "Fin_f-1")
    private String studentGroupName;
}
