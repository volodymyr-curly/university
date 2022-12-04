package ua.foxminded.university.dto.subject;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.university.dto.group.GroupNestedDTO;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.dto.teacher.TeacherNestedDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;
import static ua.foxminded.university.util.validators.ValidatorPatterns.NAME_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectResponseDTO {

    @Schema(example = "1")
    private Integer id;

    @Schema(example = "Economics And Law")
    private String name;

    @Schema(example = "2022-01-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(example = "2022-01-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ToString.Exclude
    private List<GroupNestedDTO> groups;

    @ToString.Exclude
    private List<TeacherNestedDTO> teachers;

    @ToString.Exclude
    private List<LectureResponseDTO> lectures;
}
