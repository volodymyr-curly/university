package ua.foxminded.university.dto.group;

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
import javax.validation.constraints.Size;

import static ua.foxminded.university.util.validators.ValidatorMessages.NAME_SIZE_MESSAGE;
import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupNestedDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer id;

    @Schema(example = "Fin_f-1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    @Schema(example = "Finance")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String departmentName;
}
