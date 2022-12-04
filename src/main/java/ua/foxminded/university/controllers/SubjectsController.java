package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.dto.subject.SubjectRequestDTO;
import ua.foxminded.university.dto.subject.SubjectResponseDTO;
import ua.foxminded.university.dto.teacher.TeacherRequestDTO;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.service.interfaces.SubjectService;
import ua.foxminded.university.util.mappers.GroupMapper;
import ua.foxminded.university.util.mappers.SubjectMapper;
import ua.foxminded.university.util.mappers.TeacherMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/subjects")
public class SubjectsController {

    private static final String TITLE = "Subjects";
    private static final String BUTTON = "New subject";
    private static final String CREATE_URL = "subjects/new";

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;
    private final TeacherMapper teacherMapper;
    private final GroupMapper groupMapper;

    @Autowired
    public SubjectsController(SubjectService subjectService,
                              SubjectMapper subjectMapper,
                              TeacherMapper teacherMapper,
                              GroupMapper groupMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
        this.groupMapper = groupMapper;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all subjects");
        List<SubjectResponseDTO> subjectsDTO = subjectService.findAll().stream()
            .map(subjectMapper::convertToSubjectResponseDTOWhenFindAll)
            .toList();
        model.addAttribute("subjects", subjectsDTO);
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("subjects")));
        model.addAttribute("teacher", new TeacherRequestDTO());
        model.addAttribute("group", new GroupRequestDTO());
        addListAttributesToView(model);
        addStringAttributesToView(model);
        return "subjects/all";
    }

    @GetMapping("/group/list")
    public String showByGroup(@RequestParam("id") int id, Model model) {
        log.debug("Searching by group id={}", id);
        List<SubjectResponseDTO> subjectsDTO = subjectService.findByGroup(id).stream()
            .map(subjectMapper::convertToSubjectResponseDTOWhenFindAll)
            .toList();
        model.addAttribute("subjects", subjectsDTO);
        log.debug("Show by group id {}", Objects.requireNonNull(model.getAttribute("subjects")));
        addStringAttributesToView(model);
        return "subjects/by-id";
    }

    @GetMapping("/teacher/list")
    public String showByTeacher(@RequestParam("id") int id, Model model) {
        log.debug("Searching by teacher id={}", id);
        List<SubjectResponseDTO> subjectsDTO = subjectService.findByGroup(id).stream()
            .map(subjectMapper::convertToSubjectResponseDTOWhenFindAll)
            .toList();
        model.addAttribute("subjects", subjectsDTO);
        log.debug("Show by teacher id {}", Objects.requireNonNull(model.getAttribute("subjects")));
        addStringAttributesToView(model);
        return "subjects/by-id";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.debug("Searching subject with id={}", id);
        SubjectResponseDTO subjectDTO = subjectMapper
            .convertToSubjectResponseDTO(subjectService.find(id));
        model.addAttribute("subject", subjectDTO);
        log.debug("Show {}", Objects.requireNonNull(model.getAttribute("subject")));
        return "subjects/show";
    }

    @GetMapping("/new")
    public String newSubject(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("subject", new SubjectRequestDTO());
        addListAttributesToView(model);
        log.debug("Success adding attributes");
        return "subjects/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute("subject") @Valid SubjectRequestDTO subjectDTO,
                         BindingResult bindingResult,
                         Model model) {
        log.debug("Saving {}", subjectDTO);
        addListAttributesToView(model);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "subjects/new";
        }

        try {
            Subject subject = subjectMapper.convertToSubject(subjectDTO);
            subjectService.add(subject);
            log.debug("Success when saving {}", subject);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "subjects/new";
        }

        return "redirect:/subjects";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.debug("Searching for updating subject with id={}", id);
        SubjectRequestDTO subjectDTO = subjectMapper.convertToSubjectRequestDTO(subjectService.find(id));
        model.addAttribute("subject", subjectDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("subject")));
        addListAttributesToView(model);
        return "subjects/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("subject") @Valid SubjectRequestDTO subjectDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") int id,
                         Model model) {
        log.debug("Updating subject with id={} to {}", id, subjectDTO);
        addListAttributesToView(model);
        subjectDTO.setId(id);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "subjects/edit";
        }

        try {
            Subject updatedSubject = subjectMapper.convertToSubject(subjectDTO);
            subjectService.update(id, updatedSubject);
            log.debug("Success when updating {}", updatedSubject);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "subjects/edit";
        }

        return "redirect:/subjects";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing subject with id={}", id);
        subjectService.delete(id);
        log.debug("Success when removing subject with id={}", id);
        return "redirect:/subjects";
    }

    private void addStringAttributesToView(Model model) {
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
    }

    private void addListAttributesToView(Model model) {
        log.debug("Add list attributes");
        model.addAttribute("teachers", teacherMapper.getTeachersDTO());
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        log.debug("Success adding list attributes");
    }
}
