package ua.foxminded.university.dto.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.university.domain.Employment;
import ua.foxminded.university.dto.person.PersonDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class StudentRequestDTO extends PersonDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private Integer groupId;

    @Schema(example = "Fin_f-1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String groupName;

    @Schema(enumAsRef = true)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @Enumerated(EnumType.STRING)
    private Employment educationForm;

    @Schema(example = "2022-01-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private LocalDate enrollmentDate;
}
