package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Mark;
import ua.foxminded.university.dto.mark.MarkRequestDTO;
import ua.foxminded.university.dto.mark.MarkResponseDTO;
import ua.foxminded.university.dto.student.StudentNestedDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.service.interfaces.MarkService;
import ua.foxminded.university.util.mappers.MarkMapper;
import ua.foxminded.university.util.mappers.StudentMapper;
import ua.foxminded.university.util.mappers.SubjectMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/marks")
public class MarksController {

    private static final String TITLE = "Marks";
    private static final String BUTTON = "New mark";
    private static final String CREATE_URL = "marks/new";

    private final MarkService markService;
    private final MarkMapper markMapper;
    private final StudentMapper studentMapper;
    private final SubjectMapper subjectMapper;

    @Autowired
    public MarksController(MarkService markService,
                           MarkMapper markMapper,
                           StudentMapper studentMapper,
                           SubjectMapper subjectMapper) {
        this.markService = markService;
        this.markMapper = markMapper;
        this.studentMapper = studentMapper;
        this.subjectMapper = subjectMapper;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all marks");
        List<MarkResponseDTO> marksDTO = markService.findAll().stream()
            .map(markMapper::convertToMarkResponseDTO)
            .toList();
        model.addAttribute("marks", marksDTO);
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("marks")));
        model.addAttribute("subject", new SubjectNestedDTO());
        model.addAttribute("student", new StudentNestedDTO());
        addListAttributesToView(model);
        addStringAttributesToView(model);
        return "marks/all";
    }

    @GetMapping("/subject/list")
    public String showBySubject(@RequestParam("id") int id, Model model) {
        log.debug("Searching by subject id={}", id);
        List<MarkResponseDTO> marksDTO = markService.findBySubject(id).stream()
            .map(markMapper::convertToMarkResponseDTO)
            .toList();
        model.addAttribute("marks", marksDTO);
        log.debug("Show by subject id {}", Objects.requireNonNull(model.getAttribute("marks")));
        addStringAttributesToView(model);
        return "marks/by-id";
    }

    @GetMapping("/student/list")
    public String showByStudent(@RequestParam("id") int id, Model model) {
        log.debug("Searching by student id={}", id);
        List<MarkResponseDTO> marksDTO = markService.findByStudent(id).stream()
            .map(markMapper::convertToMarkResponseDTO)
            .toList();
        model.addAttribute("marks", marksDTO);
        log.debug("Show by student id {}", Objects.requireNonNull(model.getAttribute("marks")));
        addStringAttributesToView(model);
        return "marks/by-id";
    }

    @GetMapping("/new")
    public String newMark(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("mark", new MarkRequestDTO());
        addListAttributesToView(model);
        log.debug("Success adding attributes");
        return "marks/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("mark") @Valid MarkRequestDTO markDTO,
                         BindingResult bindingResult,
                         Model model) {
        log.debug("Saving {}", markDTO);
        addListAttributesToView(model);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "marks/new";
        }

        try {
            Mark mark = markMapper.convertToMark(markDTO);
            markService.add(mark);
            log.debug("Success when saving {}", mark);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "marks/new";
        }

        return "redirect:/marks";
    }

    @GetMapping("/{markId}/edit")
    public String edit(Model model, @PathVariable("markId") int id) {
        log.debug("Searching for updating mark with id={}", id);
        MarkRequestDTO markDTO = markMapper.convertToMarkRequestDTO(markService.find(id));
        model.addAttribute("mark", markDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("mark")));
        addListAttributesToView(model);
        return "marks/edit";
    }

    @PostMapping("/{markId}")
    public String update(@ModelAttribute("mark") @Valid MarkRequestDTO markDTO,
                         BindingResult bindingResult,
                         @PathVariable("markId") int id,
                         Model model) {
        log.debug("Updating mark with id={} to {}", id, markDTO);
        addListAttributesToView(model);
        markDTO.setId(id);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "marks/edit";
        }

        try {
            Mark updatedMark = markMapper.convertToMark(markDTO);
            markService.update(id, updatedMark);
            log.debug("Success when updating {}", updatedMark);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "marks/edit";
        }

        return "redirect:/marks";
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing mark with id={}", id);
        markService.delete(id);
        log.debug("Success when removing mark with id={}", id);
        return "redirect:/marks";
    }

    private void addStringAttributesToView(Model model) {
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
    }

    private void addListAttributesToView(Model model) {
        log.debug("Add list attributes");
        model.addAttribute("subjects", subjectMapper.getSubjectsDTO());
        model.addAttribute("students", studentMapper.getStudentDTO());
        log.debug("Success adding list attributes");
    }
}
