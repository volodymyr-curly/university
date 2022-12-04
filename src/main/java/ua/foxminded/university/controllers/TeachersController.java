package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Teacher;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.dto.teacher.TeacherRequestDTO;
import ua.foxminded.university.dto.teacher.TeacherResponseDTO;
import ua.foxminded.university.dto.subject.SubjectRequestDTO;
import ua.foxminded.university.service.interfaces.*;
import ua.foxminded.university.util.mappers.DepartmentMapper;
import ua.foxminded.university.util.mappers.EmployeeMapper;
import ua.foxminded.university.util.mappers.SubjectMapper;
import ua.foxminded.university.util.mappers.TeacherMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/teachers")
public class TeachersController {

    private static final String TITLE = "Teachers";
    private static final String BUTTON = "New teacher";
    private static final String CREATE_URL = "teachers/new";

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;
    private final EmployeeMapper employeeMapper;
    private final DepartmentMapper departmentMapper;
    private final SubjectMapper subjectMapper;

    @Autowired
    public TeachersController(TeacherService teacherService,
                              TeacherMapper teacherMapper,
                              EmployeeMapper employeeMapper,
                              DepartmentMapper departmentMapper,
                              SubjectMapper subjectMapper) {
        this.teacherService = teacherService;
        this.departmentMapper = departmentMapper;
        this.teacherMapper = teacherMapper;
        this.employeeMapper = employeeMapper;
        this.subjectMapper = subjectMapper;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all teachers");
        List<TeacherRequestDTO> teachers = teacherService.findAll().stream()
            .map(teacherMapper::convertToTeacherDTO)
            .toList();
        model.addAttribute("teachers", teachers);
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("teachers")));
        model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
        model.addAttribute("department", new DepartmentRequestDTO());
        model.addAttribute("subjects", subjectMapper.getSubjectsDTO());
        model.addAttribute("subject", new SubjectRequestDTO());
        addStringAttributesToView(model);
        return "teachers/all";
    }

    @GetMapping("/department/list")
    public String showByDepartment(@RequestParam("id") int id, Model model) {
        log.debug("Searching by department id={}", id);
        List<TeacherRequestDTO> teachers = teacherService.findByDepartment(id).stream()
            .map(teacherMapper::convertToTeacherDTO)
            .toList();
        model.addAttribute("teachers", teachers);
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("teachers")));
        addStringAttributesToView(model);
        return "teachers/by-id";
    }

    @GetMapping("/subject/list")
    public String showBySubject(@RequestParam("id") int id, Model model) {
        log.debug("Searching by subject id={}", id);
        List<TeacherRequestDTO> teachers = teacherService.findBySubject(id).stream()
            .map(teacherMapper::convertToTeacherDTO)
            .toList();
        model.addAttribute("teachers", teachers);
        log.debug("Show by subject id {}", Objects.requireNonNull(model.getAttribute("teachers")));
        addStringAttributesToView(model);
        return "teachers/by-id";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.debug("Searching teacher with id={}", id);
        TeacherResponseDTO teacher = teacherMapper.convertToTeacherResponseDTO(teacherService.find(id));
        model.addAttribute("teacher", teacher);
        log.debug("Show {}", Objects.requireNonNull(model.getAttribute("teacher")));
        return "teachers/show";
    }

    @GetMapping("/new")
    public String newTeacher(Model model) {
        log.debug("Add attributes then save");
        addListAttributesToView(model);
        model.addAttribute("teacher", new TeacherRequestDTO());
        log.debug("Success adding attributes");
        return "teachers/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("teacher") @Valid TeacherRequestDTO teacherDTO,
                         BindingResult bindingResult,
                         Model model) {
        log.debug("Saving {}", teacherDTO);

        if (bindingResult.hasErrors()) {
            addListAttributesToView(model);
            log.debug("There are errors in bindingResult");
            return "teachers/new";
        }
        Teacher teacher = teacherMapper.convertToTeacher(teacherDTO);
        teacherService.add(teacher);
        log.debug("Success when saving {}", teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.debug("Searching for updating teacher with id={}", id);
        TeacherRequestDTO teacherDTO = teacherMapper.convertToTeacherDTO(teacherService.find(id));
        model.addAttribute("teacher", teacherDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("teacher")));
        model.addAttribute("subjects", subjectMapper.getSubjectsDTO());
        return "teachers/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("teacher") TeacherRequestDTO teacherDTO,
                         @PathVariable("id") int id) {
        log.debug("Updating teacher with id={} to {}", id, teacherDTO);
        Teacher updatedTeacher = teacherMapper.convertToTeacher(teacherDTO);
        teacherService.update(id, updatedTeacher);
        log.debug("Success when updating {}", updatedTeacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing teacher with id={}", id);
        teacherService.delete(id);
        log.debug("Success when removing teacher with id={}", id);
        return "redirect:/teachers";
    }

    private void addStringAttributesToView(Model model) {
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
    }

    private void addListAttributesToView(Model model) {
        log.debug("Add list attributes");
        model.addAttribute("employees", employeeMapper.getNoTeachersDTO(model));
        model.addAttribute("subjects", subjectMapper.getSubjectsDTO());
        log.debug("Success adding list attributes");
    }
}
