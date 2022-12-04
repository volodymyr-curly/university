package ua.foxminded.university.dto.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import ua.foxminded.university.dto.employee.EmployeeNestedDTO;
import ua.foxminded.university.dto.group.GroupNestedDTO;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO {

    @Schema(example = "1")
    private Integer id;

    @Schema(example = "Finance")
    private String name;

    @Schema(example = "Economics")
    private String facultyName;

    @ToString.Exclude
    private List<GroupNestedDTO> groups;

    @ToString.Exclude
    private List<EmployeeNestedDTO> employees;
}
