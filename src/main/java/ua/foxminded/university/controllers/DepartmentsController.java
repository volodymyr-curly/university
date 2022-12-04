package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.dto.department.DepartmentResponseDTO;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.dto.faculty.FacultyRequestDTO;
import ua.foxminded.university.service.interfaces.DepartmentService;
import ua.foxminded.university.util.mappers.DepartmentMapper;
import ua.foxminded.university.util.mappers.FacultyMapper;
import ua.foxminded.university.util.validators.DepartmentValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/departments")
public class DepartmentsController {

    private static final String TITLE = "Departments";
    private static final String BUTTON = "New department";
    private static final String CREATE_URL = "departments/new";

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final DepartmentValidator departmentValidator;
    private final FacultyMapper facultyMapper;

    @Autowired
    public DepartmentsController(DepartmentService departmentService,
                                 DepartmentMapper departmentMapper,
                                 DepartmentValidator departmentValidator,
                                 FacultyMapper facultyMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
        this.departmentValidator = departmentValidator;
        this.facultyMapper = facultyMapper;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all departments");
        List<DepartmentResponseDTO> departmentsDTO = departmentService.findAll().stream()
            .map(departmentMapper::convertToDepartmentResponseDTO)
            .toList();
        model.addAttribute("departments", departmentsDTO);
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("departments")));
        model.addAttribute("faculties", facultyMapper.getFacultyDTO());
        model.addAttribute("faculty", new FacultyRequestDTO());
        addStringAttributesToView(model);
        return "departments/all";
    }

    @GetMapping("/faculty/list")
    public String showByFaculty(@RequestParam("id") int id, Model model) {
        log.debug("Searching by faculty id={}", id);
        List<DepartmentResponseDTO> departmentsDTO = departmentService.findByFaculty(id).stream()
            .map(departmentMapper::convertToDepartmentResponseDTO)
            .toList();
        model.addAttribute("departments", departmentsDTO);
        log.debug("Show by faculty id {}", Objects.requireNonNull(model.getAttribute("departments")));
        addStringAttributesToView(model);
        return "departments/by-id";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.debug("Searching department with id={}", id);
        DepartmentResponseDTO departmentDetailedDTO = departmentMapper.convertToDepartmentResponseDTO(departmentService.find(id));
        model.addAttribute("department", departmentDetailedDTO);
        log.debug("Show {}", Objects.requireNonNull(model.getAttribute("department")));
        return "departments/show";
    }

    @GetMapping("/new")
    public String newDepartment(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("department", new DepartmentRequestDTO());
        model.addAttribute("faculties", facultyMapper.getFacultyDTO());
        log.debug("Success adding attributes");
        return "departments/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("department") @Valid DepartmentRequestDTO departmentDTO,
                         BindingResult bindingResult,
                         Model model) {
        log.debug("Saving {}", departmentDTO);
        departmentValidator.validate(departmentDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            model.addAttribute("faculties", facultyMapper.getFacultyDTO());
            return "departments/new";
        }

        Department department = departmentMapper.convertToDepartment(departmentDTO);
        departmentService.add(department);
        log.debug("Success when saving {}", department);
        return "redirect:/departments";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.debug("Searching for updating department with id={}", id);
        DepartmentRequestDTO departmentDTO = departmentMapper.convertToDepartmentRequestDTO(departmentService.find(id));
        model.addAttribute("department", departmentDTO);
        model.addAttribute("faculties", facultyMapper.getFacultyDTO());
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("department")));
        return "departments/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("department") @Valid DepartmentRequestDTO departmentDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") int id,
                         Model model) {
        log.debug("Updating department with id={} to {}", id, departmentDTO);
        departmentDTO.setId(id);
        departmentValidator.validate(departmentDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            model.addAttribute("faculties", facultyMapper.getFacultyDTO());
            return "departments/edit";
        }
        Department updatedDepartment = departmentMapper.convertToDepartment(departmentDTO);
        departmentService.update(id, updatedDepartment);
        log.debug("Success when updating {}", updatedDepartment);
        return "redirect:/departments";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing department with id={}", id);
        departmentService.delete(id);
        log.debug("Success when removing department with id={}", id);
        return "redirect:/departments";
    }

    private void addStringAttributesToView(Model model) {
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
    }
}
