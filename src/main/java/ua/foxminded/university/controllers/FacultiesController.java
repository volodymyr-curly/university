package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Faculty;
import ua.foxminded.university.dto.faculty.FacultyRequestDTO;
import ua.foxminded.university.service.interfaces.FacultyService;
import ua.foxminded.university.util.mappers.FacultyMapper;
import ua.foxminded.university.util.validators.FacultyValidator;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/faculties")
public class FacultiesController {

    private static final String TITLE = "Faculties";
    private static final String BUTTON = "New faculty";
    private static final String CREATE_URL = "faculties/new";

    private final FacultyService facultyService;
    private final FacultyMapper facultyMapper;
    private final FacultyValidator facultyValidator;

    @Autowired
    public FacultiesController(FacultyService facultyService, FacultyMapper facultyMapper, FacultyValidator facultyValidator) {
        this.facultyService = facultyService;
        this.facultyMapper = facultyMapper;
        this.facultyValidator = facultyValidator;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all faculties");
        model.addAttribute("faculties", facultyMapper.getFacultiesDTO());
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("faculties")));
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
        return "faculties/all";
    }

    @GetMapping("/new")
    public String newFaculty(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("faculty", new FacultyRequestDTO());
        log.debug("Success adding attributes");
        return "faculties/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("faculty") @Valid FacultyRequestDTO facultyDTO,
                         BindingResult bindingResult) {
        log.debug("Saving {}", facultyDTO);
        facultyValidator.validate(facultyDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "faculties/new";
        }

        Faculty faculty = facultyMapper.convertToFaculty(facultyDTO);
        facultyService.add(faculty);
        log.debug("Success when saving {}", faculty);
        return "redirect:/faculties";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.debug("Searching for updating faculty with id={}", id);
        FacultyRequestDTO facultyDTO = facultyMapper.convertToFacultyRequestDTO(facultyService.find(id));
        model.addAttribute("faculty", facultyDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("faculty")));
        return "faculties/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("faculty") @Valid FacultyRequestDTO facultyDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        log.debug("Updating faculty with id={} to {}", id, facultyDTO);
        facultyDTO.setId(id);
        facultyValidator.validate(facultyDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "faculties/edit";
        }

        Faculty updatedFaculty = facultyMapper.convertToFaculty(facultyDTO);
        facultyService.update(id, updatedFaculty);
        log.debug("Success when updating {}", updatedFaculty);
        return "redirect:/faculties";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing faculty with id={}", id);
        facultyService.delete(id);
        log.debug("Success when removing faculty with id={}", id);
        return "redirect:/faculties";
    }
}
