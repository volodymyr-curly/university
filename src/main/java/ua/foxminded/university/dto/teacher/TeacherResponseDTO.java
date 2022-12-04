package ua.foxminded.university.dto.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.foxminded.university.domain.*;
import ua.foxminded.university.dto.employee.EmployeeDTO;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponseDTO {

    @Schema(example = "1")
    private Integer id;

    private EmployeeDTO employee;

    @Schema(enumAsRef = true)
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @ToString.Exclude
    private List<SubjectNestedDTO> subjects;

    @ToString.Exclude
    private List<LectureResponseDTO> lectures;
}
