package ua.foxminded.university.dto.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;
import static ua.foxminded.university.util.validators.ValidatorPatterns.NAME_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequestDTO {

    @Schema(example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(example = "Finance")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String name;

    @Schema(example = "2")
    @JsonDeserialize(using = CustomDeserializer.class)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private Integer facultyId;

    @Schema(example = "Economics")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String facultyName;
}
