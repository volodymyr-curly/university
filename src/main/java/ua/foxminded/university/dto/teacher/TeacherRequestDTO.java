package ua.foxminded.university.dto.teacher;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.foxminded.university.domain.Degree;
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.dto.employee.EmployeeNestedDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.List;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherRequestDTO {

    @Schema(example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Valid
    private EmployeeNestedDTO employee;

    @Schema(enumAsRef = true)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @Valid
    @ToString.Exclude
    List<SubjectNestedDTO> subjects;
}
