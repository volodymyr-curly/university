package ua.foxminded.university.dto.teacher;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxminded.university.domain.JobTitle;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherNestedDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer id;

    @Schema(example = "Ivan")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String employeeFirstName;

    @Schema(example = "Ivanenko")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String employeeLastName;

    @Schema(example = "Finance")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String employeeDepartmentName;

    @Schema(enumAsRef = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private JobTitle employeeJobTitle;
}
