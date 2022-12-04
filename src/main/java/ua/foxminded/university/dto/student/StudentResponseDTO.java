package ua.foxminded.university.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.university.domain.Employment;
import ua.foxminded.university.dto.mark.MarkNestedDTO;
import ua.foxminded.university.dto.person.PersonDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponseDTO extends PersonDTO {

    @Schema(example = "Fin_f-1")
    private String groupName;

    @Schema(enumAsRef = true)
    @Enumerated(EnumType.STRING)
    private Employment educationForm;

    @Schema(example = "2022-01-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;

    @ToString.Exclude
    private List<MarkNestedDTO> marks;
}
