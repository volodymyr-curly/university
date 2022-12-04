package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.dto.student.StudentRequestDTO;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.dto.student.StudentResponseDTO;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.service.interfaces.StudentService;
import ua.foxminded.university.util.mappers.GroupMapper;
import ua.foxminded.university.util.mappers.StudentMapper;
import ua.foxminded.university.util.validators.PersonValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/students")
public class StudentsController {

    private static final String TITLE = "Students";
    private static final String BUTTON = "New student";
    private static final String CREATE_URL = "students/new";

    private final StudentService studentService;
    private final PersonValidator personValidator;
    private final StudentMapper studentMapper;
    private final GroupMapper groupMapper;

    @Autowired
    public StudentsController(StudentService studentService,
                              PersonValidator personValidator,
                              StudentMapper studentMapper,
                              GroupMapper groupMapper) {
        this.studentService = studentService;
        this.personValidator = personValidator;
        this.studentMapper = studentMapper;
        this.groupMapper = groupMapper;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all students");
        List<StudentResponseDTO> studentsDTO = studentService.findAll().stream()
            .map(studentMapper::convertToDTOWhenFindAll)
            .toList();
        model.addAttribute("students", studentsDTO);
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("students")));
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        model.addAttribute("group", new GroupRequestDTO());
        addStringAttributesToView(model);
        return "students/all";
    }

    @GetMapping("/group/list")
    public String showByGroup(@RequestParam("id") int id, Model model) {
        log.debug("Searching by group id={}", id);
        List<StudentResponseDTO> studentsDTO = studentService.findByGroup(id).stream()
            .map(studentMapper::convertToDTOWhenFindOne)
            .toList();
        model.addAttribute("students", studentsDTO);
        log.debug("Show by group id {}", Objects.requireNonNull(model.getAttribute("students")));
        addStringAttributesToView(model);
        return "students/by-id";
    }

    @GetMapping("/{studentId}")
    public String show(@PathVariable("studentId") int id, Model model) {
        log.debug("Searching student with id={}", id);
        StudentResponseDTO studentDTO = studentMapper.convertToDTOWhenFindOne(studentService.find(id));
        model.addAttribute("student", studentDTO);
        log.debug("Show {}", Objects.requireNonNull(model.getAttribute("student")));
        return "students/show";
    }

    @GetMapping("/new")
    public String newStudent(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        model.addAttribute("student", new StudentRequestDTO());
        log.debug("Success adding attributes");
        return "students/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("student") @Valid StudentRequestDTO studentDTO,
                         BindingResult bindingResult,
                         Model model) {
        log.debug("Saving {}", studentDTO);
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        personValidator.validate(studentDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "students/new";
        }

        try {
            Student student = studentMapper.convertToStudent(studentDTO);
            studentService.add(student);
            log.debug("Success when saving {}", student);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "students/new";
        }

        return "redirect:/students";
    }

    @GetMapping("/{studentId}/edit")
    public String edit(Model model, @PathVariable("studentId") int id) {
        log.debug("Searching for updating student with id={}", id);
        StudentRequestDTO studentDTO = studentMapper.convertToStudentRequestDTO(studentService.find(id));
        model.addAttribute("student", studentDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("student")));
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        return "students/edit";
    }

    @PostMapping("/{studentId}")
    public String update(@ModelAttribute("student") @Valid StudentRequestDTO studentDTO,
                         BindingResult bindingResult,
                         @PathVariable("studentId") int id,
                         Model model) {
        log.debug("Updating student with id={} to {}", id, studentDTO);
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        studentDTO.setId(id);
        personValidator.validate(studentDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "students/edit";
        }

        try {
            Student updatedStudent = studentMapper.convertToStudent(studentDTO);
            studentService.update(id, updatedStudent);
            log.debug("Success when updating {}", updatedStudent);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "students/edit";
        }

        return "redirect:/students";
    }

    @GetMapping("/{studentId}/delete")
    public String delete(@PathVariable("studentId") int id) {
        log.debug("Removing student with id={}", id);
        studentService.delete(id);
        log.debug("Success when removing student with id={}", id);
        return "redirect:/students";
    }

    private void addStringAttributesToView(Model model) {
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
    }
}
