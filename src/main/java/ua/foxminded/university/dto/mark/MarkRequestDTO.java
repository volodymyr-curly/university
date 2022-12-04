package ua.foxminded.university.dto.mark;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.foxminded.university.domain.MarkValue;
import ua.foxminded.university.dto.student.StudentNestedDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkRequestDTO {

    @Schema(example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(enumAsRef = true)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @Enumerated(EnumType.STRING)
    private MarkValue value;

    @Valid
    private SubjectNestedDTO subject;

    @Valid
    private StudentNestedDTO student;
}
