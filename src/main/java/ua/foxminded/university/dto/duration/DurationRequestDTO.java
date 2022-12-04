package ua.foxminded.university.dto.duration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxminded.university.util.validators.interfaces.Persist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DurationRequestDTO {

    @Schema(example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(example = "09:00")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    private String startTime;

    @Schema(example = "10:20")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    private String endTime;
}
