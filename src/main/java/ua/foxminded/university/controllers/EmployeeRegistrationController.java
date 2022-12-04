package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.dto.employee.EmployeeDTO;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.service.interfaces.EmployeeService;
import ua.foxminded.university.util.mappers.DepartmentMapper;
import ua.foxminded.university.util.mappers.EmployeeMapper;
import ua.foxminded.university.util.validators.PersonValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/employee")
public class EmployeeRegistrationController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final DepartmentMapper departmentMapper;
    private final PersonValidator personValidator;

    @Autowired
    public EmployeeRegistrationController(EmployeeService employeeService,
                                          EmployeeMapper employeeMapper,
                                          DepartmentMapper departmentMapper,
                                          PersonValidator personValidator) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.departmentMapper = departmentMapper;
        this.personValidator = personValidator;
    }

    @GetMapping("/registration")
    public String employeeForm(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("employee", new EmployeeDTO());
        model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
        log.debug("Success adding attributes");
        return "registration/employee";
    }

    @PostMapping()
    public String registerEmployee(@ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
                                   BindingResult bindingResult,
                                   Model model) {
        log.debug("Register {}", employeeDTO);
        model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
        personValidator.validate(employeeDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "registration/employee";
        }

        try {
            Employee employee = employeeMapper.convertToEmployee(employeeDTO);
            employeeService.add(employee);
            log.debug("Success when register {}", employee);

        } catch (EntityExistsException e) {
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "registration/employee";
        }

        return "redirect:/auth/login";
    }

}
