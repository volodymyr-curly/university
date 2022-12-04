package ua.foxminded.university.dto.faculty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.foxminded.university.dto.department.DepartmentNestedDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacultyResponseDTO {

    @Schema(example = "1")
    private Integer id;

    @Schema(example = "Economics")
    private String name;

    @ToString.Exclude
    private List<DepartmentNestedDTO> departments;
}
