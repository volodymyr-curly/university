package ua.foxminded.university.dto.faculty;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;
import static ua.foxminded.university.util.validators.ValidatorPatterns.NAME_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacultyRequestDTO {

    @Schema(example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(example = "Economics")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String name;
}
