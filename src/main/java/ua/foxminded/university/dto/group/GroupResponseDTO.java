package ua.foxminded.university.dto.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.foxminded.university.domain.Lecture;
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.dto.student.StudentNestedDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupResponseDTO {

    @Schema(example = "1")
    private Integer id;

    @Schema(example = "Fin_f-1")
    private String name;

    @Schema(example = "Finance")
    private String departmentName;

    @ToString.Exclude
    private List<StudentNestedDTO> students;

    @ToString.Exclude
    private List<SubjectNestedDTO> subjects;

    @ToString.Exclude
    private List<LectureResponseDTO> lectures;
}
