package ua.foxminded.university.dto.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;
import static ua.foxminded.university.util.validators.ValidatorPatterns.NAME_PATTERN;

@Data
public class DepartmentNestedDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer id;

    @Schema(example = "Finance")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;
}
