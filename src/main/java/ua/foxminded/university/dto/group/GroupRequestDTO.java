package ua.foxminded.university.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;
import static ua.foxminded.university.util.validators.ValidatorPatterns.GROUP_NAME_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequestDTO {

    @Schema(example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(example = "Fin_f-1")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = GROUP_NAME_PATTERN, message = GROUP_NAME_PATTERN_MESSAGE)
    private String name;

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer departmentId;

    @Schema(example = "Finance")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String departmentName;

    @Valid
    @ToString.Exclude
    private List<SubjectNestedDTO> subjects;

    @Valid
    @ToString.Exclude
    private List<LectureResponseDTO> lectures;
}
