package ua.foxminded.university.dto.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ua.foxminded.university.dto.person.PersonNestedDTO;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class StudentNestedDTO extends PersonNestedDTO {

    @Schema(example = "Fin_f-1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String groupName;
}
