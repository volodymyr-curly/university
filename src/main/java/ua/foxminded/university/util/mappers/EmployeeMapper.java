package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.dto.employee.EmployeeDTO;
import ua.foxminded.university.service.interfaces.EmployeeService;

import java.util.List;
import java.util.Objects;

import static ua.foxminded.university.util.validators.ValidatorMessages.NO_TEACHERS_MESSAGE;

@Component
public class EmployeeMapper {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeMapper(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public EmployeeDTO convertToDTOWhenFindAll(Employee employee) {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
            .id(employee.getId())
            .firstName(employee.getFirstName())
            .lastName(employee.getLastName())
            .jobTitle(employee.getJobTitle())
            .build();
        onDeleteDepartmentSetNull(employee, employeeDTO);
        return employeeDTO;
    }

    public EmployeeDTO convertToDTOWhenFindOne(Employee employee) {
        PropertyMap<Employee, EmployeeDTO> FindOne = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
                skip(destination.getDepartmentId());
            }
        };

        EmployeeDTO employeeDTO = new ModelMapper().addMappings(FindOne).map(employee);
        onDeleteDepartmentSetNull(employee, employeeDTO);
        return employeeDTO;
    }

    public Employee convertToEmployee(EmployeeDTO employeeDTO) {
        return new ModelMapper().map(employeeDTO, Employee.class);
    }

    public List<EmployeeDTO> getNoTeachersDTO(Model model) {
        List<EmployeeDTO> employees = employeeService.findNotTeachers().stream()
            .map(this::convertToDTOWhenFindAll)
            .toList();

        if (employees.isEmpty()) {
            model.addAttribute("emptyEmployeeList", NO_TEACHERS_MESSAGE);
        }

        return employees;
    }

    private void onDeleteDepartmentSetNull(Employee employee, EmployeeDTO employeeDTO) {
        if (Objects.isNull(employee.getDepartment())) {
            employeeDTO.setDepartmentName(null);
        } else {
            employeeDTO.setDepartmentName(employee.getDepartment().getName());
        }
    }
}
