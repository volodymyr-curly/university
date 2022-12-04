package ua.foxminded.university.dto.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.validation.constraints.NotNull;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class PersonNestedDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer id;

    @Schema(example = "Ivan")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String firstName;

    @Schema(example = "Ivanenko")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastName;
}
