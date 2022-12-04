package ua.foxminded.university.dto.employee;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.university.domain.Employment;
import ua.foxminded.university.domain.JobTitle;
import ua.foxminded.university.dto.person.PersonDTO;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO extends PersonDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer departmentId;

    @Schema(example = "Finance")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String departmentName;

    @Schema(enumAsRef = true)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;

    @Schema(enumAsRef = true)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @Enumerated(EnumType.STRING)
    private Employment employmentType;

    @Schema(example = "2022-01-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private LocalDate employmentDate;
}
