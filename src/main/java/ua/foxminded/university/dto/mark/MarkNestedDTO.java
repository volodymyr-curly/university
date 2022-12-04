package ua.foxminded.university.dto.mark;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ua.foxminded.university.domain.MarkValue;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
public class MarkNestedDTO {

    @Schema(example = "1")
    @JsonDeserialize(using = CustomDeserializer.class)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private Integer id;

    @Schema(enumAsRef = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private MarkValue value;

    @Schema(example = "Economics And Law")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String subjectName;

    @Schema(example = "2022-01-31")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate subjectEndDate;
}
