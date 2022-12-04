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
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.dto.student.StudentRequestDTO;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.service.interfaces.StudentService;
import ua.foxminded.university.util.mappers.GroupMapper;
import ua.foxminded.university.util.mappers.StudentMapper;
import ua.foxminded.university.util.validators.PersonValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/student")
public class StudentRegistrationController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final GroupMapper groupMapper;
    private final PersonValidator personValidator;

    @Autowired
    public StudentRegistrationController(StudentService studentService,
                                         StudentMapper studentMapper,
                                         GroupMapper groupMapper,
                                         PersonValidator personValidator) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.groupMapper = groupMapper;
        this.personValidator = personValidator;
    }

    @GetMapping("/registration")
    public String studentForm(Model model) {
        log.debug("Add attributes then register");
        model.addAttribute("student", new StudentRequestDTO());
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        log.debug("Success adding attributes");
        return "registration/student";
    }

    @PostMapping()
    public String registerStudent(@ModelAttribute("student") @Valid StudentRequestDTO studentDTO,
                                  BindingResult bindingResult,
                                  Model model) {
        log.debug("Register {}", studentDTO);
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        personValidator.validate(studentDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "registration/student";
        }

        try {
            Student student = studentMapper.convertToStudent(studentDTO);
            studentService.add(student);
            log.debug("Success when register {}", student);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "registration/student";
        }

        return "redirect:/auth/login";
    }
}
