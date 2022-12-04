package ua.foxminded.university.dto.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.validation.constraints.NotNull;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomNestedDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer id;

    @Schema(example = "101")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer number;

    @Schema(example = "50")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer capacity;
}
