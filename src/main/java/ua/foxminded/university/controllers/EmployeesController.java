package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.dto.employee.EmployeeDTO;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.service.interfaces.EmployeeService;
import ua.foxminded.university.util.mappers.DepartmentMapper;
import ua.foxminded.university.util.mappers.EmployeeMapper;
import ua.foxminded.university.util.validators.PersonValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/employees")
public class EmployeesController {

    private static final String TITLE = "Employees";
    private static final String BUTTON = "New employee";
    private static final String CREATE_URL = "employees/new";

    private final EmployeeService employeeService;
    private final PersonValidator personValidator;
    private final EmployeeMapper employeeMapper;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public EmployeesController(EmployeeService employeeService,
                               PersonValidator personValidator,
                               EmployeeMapper employeeMapper,
                               DepartmentMapper departmentMapper) {
        this.employeeService = employeeService;
        this.personValidator = personValidator;
        this.employeeMapper = employeeMapper;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all employees");
        List<EmployeeDTO> employeesDTO = employeeService.findAll().stream()
            .map(employeeMapper::convertToDTOWhenFindAll)
            .toList();
        model.addAttribute("employees", employeesDTO);
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("employees")));
        model.addAttribute("department", new DepartmentRequestDTO());
        model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
        addStringAttributesToView(model);
        return "employees/all";
    }

    @GetMapping("/department/list")
    public String showByDepartment(@RequestParam("id") int id, Model model) {
        log.debug("Searching by department id={}", id);
        List<EmployeeDTO> employeesDTO = employeeService.findByDepartment(id).stream()
            .map(employeeMapper::convertToDTOWhenFindAll)
            .toList();
        model.addAttribute("employees", employeesDTO);
        log.debug("Show by department id {}", Objects.requireNonNull(model.getAttribute("employees")));
        addStringAttributesToView(model);
        return "employees/by-id";
    }

    @GetMapping("/{employeeId}")
    public String show(@PathVariable("employeeId") int id, Model model) {
        log.debug("Searching employee with id={}", id);
        EmployeeDTO employeeDTO = employeeMapper.convertToDTOWhenFindOne(employeeService.find(id));
        model.addAttribute("employee", employeeDTO);
        log.debug("Show {}", Objects.requireNonNull(model.getAttribute("employee")));
        return "employees/show";
    }

    @GetMapping("/new")
    public String newEmployee(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
        model.addAttribute("employee", new EmployeeDTO());
        log.debug("Success adding attributes");
        return "employees/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
                         BindingResult bindingResult,
                         Model model) {
        log.debug("Saving {}", employeeDTO);
        model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
        personValidator.validate(employeeDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "employees/new";
        }

        try {
            Employee employee = employeeMapper.convertToEmployee(employeeDTO);
            employeeService.add(employee);
            log.debug("Success when saving {}", employee);

        } catch (EntityExistsException e) {
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "employees/new";
        }

        return "redirect:/employees";
    }

    @GetMapping("/{employeeId}/edit")
    public String edit(Model model, @PathVariable("employeeId") int id) {
        log.debug("Searching for updating employee with id={}", id);
        EmployeeDTO employeeDTO = employeeMapper.convertToDTOWhenFindOne(employeeService.find(id));
        model.addAttribute("employee", employeeDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("employee")));
        model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
        return "employees/edit";
    }

    @PostMapping("/{employeeId}")
    public String update(@ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
                         BindingResult bindingResult,
                         @PathVariable("employeeId") int id,
                         Model model) {
        log.debug("Updating employee with id={} to {}", id, employeeDTO);
        employeeDTO.setId(id);
        personValidator.validate(employeeDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
            return "employees/edit";
        }

        try {
            Employee updatedEmployee = employeeMapper.convertToEmployee(employeeDTO);
            employeeService.update(id, updatedEmployee);
            log.debug("Success when updating {}", updatedEmployee);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "employees/edit";
        }

        return "redirect:/employees";
    }

    @GetMapping("/{employeeId}/delete")
    public String delete(@PathVariable("employeeId") int id) {
        log.debug("Removing employee with id={}", id);
        employeeService.delete(id);
        log.debug("Success when removing employee with id={}", id);
        return "redirect:/employees";
    }

    private void addStringAttributesToView(Model model) {
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
    }
}
