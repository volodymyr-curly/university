package ua.foxminded.university.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.foxminded.university.domain.JobTitle;
import ua.foxminded.university.dto.person.PersonNestedDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeNestedDTO extends PersonNestedDTO {

    @Schema(example = "Finance")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String departmentName;

    @Schema(enumAsRef = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;
}
