package ua.foxminded.university.dto.duration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxminded.university.util.converters.CustomDeserializer;
import ua.foxminded.university.util.validators.interfaces.Persist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DurationNestedDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer id;

    @Schema(example = "09:00")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String startTime;

    @Schema(example = "10:20")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String endTime;
}
