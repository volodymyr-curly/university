package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.dto.department.DepartmentNestedDTO;
import ua.foxminded.university.dto.department.DepartmentResponseDTO;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.service.interfaces.DepartmentService;

import java.util.List;

@Component
public class DepartmentMapper {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentMapper(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public Department convertToDepartment(DepartmentRequestDTO departmentDTO) {
        return new ModelMapper().map(departmentDTO, Department.class);
    }

    public DepartmentResponseDTO convertToDepartmentResponseDTO(Department department) {
        return new ModelMapper().map(department, DepartmentResponseDTO.class);
    }

    public DepartmentRequestDTO convertToDepartmentRequestDTO(Department department) {
        return new ModelMapper().map(department, DepartmentRequestDTO.class);
    }

    public DepartmentNestedDTO convertToDepartmentNestedDTO(Department department) {
        return new ModelMapper().map(department, DepartmentNestedDTO.class);
    }

    public List<DepartmentNestedDTO> getDepartmentsDTO() {
        return departmentService.findAll().stream()
            .map(this::convertToDepartmentNestedDTO)
            .toList();
    }
}
