package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Group;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.dto.group.GroupResponseDTO;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.dto.subject.SubjectRequestDTO;
import ua.foxminded.university.service.interfaces.*;
import ua.foxminded.university.util.mappers.DepartmentMapper;
import ua.foxminded.university.util.mappers.GroupMapper;
import ua.foxminded.university.util.mappers.LectureMapper;
import ua.foxminded.university.util.mappers.SubjectMapper;
import ua.foxminded.university.util.validators.GroupValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/groups")
public class GroupsController {

    private static final String TITLE = "Groups";
    private static final String BUTTON = "New group";
    private static final String CREATE_URL = "groups/new";

    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final DepartmentMapper departmentMapper;
    private final SubjectMapper subjectMapper;
    private final LectureMapper lectureMapper;
    private final GroupValidator groupValidator;

    @Autowired
    public GroupsController(GroupService groupService,
                            GroupMapper groupMapper,
                            DepartmentMapper departmentMapper,
                            SubjectMapper subjectMapper,
                            LectureMapper lectureMapper,
                            GroupValidator groupValidator) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
        this.departmentMapper = departmentMapper;
        this.subjectMapper = subjectMapper;
        this.lectureMapper = lectureMapper;
        this.groupValidator = groupValidator;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all groups");
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("groups")));
        model.addAttribute("department", new DepartmentRequestDTO());
        model.addAttribute("subject", new SubjectRequestDTO());
        model.addAttribute("lecture", new LectureResponseDTO());
        addListAttributesToView(model);
        addStringAttributesToView(model);
        return "groups/all";
    }

    @GetMapping("/department/list")
    public String showByDepartment(@RequestParam("id") int id, Model model) {
        log.debug("Searching by newDepartment id={}", id);
        List<GroupResponseDTO> groupsDTO = groupService.findByDepartment(id).stream()
            .map(groupMapper::convertToGroupResponseDTOWhenFindAll)
            .toList();
        model.addAttribute("groups", groupsDTO);
        log.debug("Show by newDepartment id {}", Objects.requireNonNull(model.getAttribute("groups")));
        addStringAttributesToView(model);
        return "groups/by-id";
    }

    @GetMapping("/subject/list")
    public String showBySubject(@RequestParam("id") int id, Model model) {
        log.debug("Searching by subject id={}", id);
        List<GroupResponseDTO> groupsDTO = groupService.findBySubject(id).stream()
            .map(groupMapper::convertToGroupResponseDTOWhenFindAll)
            .toList();
        model.addAttribute("groups", groupsDTO);
        log.debug("Show by subject id {}", Objects.requireNonNull(model.getAttribute("groups")));
        addStringAttributesToView(model);
        return "groups/by-id";
    }

    @GetMapping("/lecture/list")
    public String showByLecture(@RequestParam("id") int id, Model model) {
        log.debug("Searching by newLecture id={}", id);
        List<GroupResponseDTO> groupsDTO = groupService.findByLecture(id).stream()
            .map(groupMapper::convertToGroupResponseDTOWhenFindAll)
            .toList();
        model.addAttribute("groups", groupsDTO);
        log.debug("Show by newLecture id {}", Objects.requireNonNull(model.getAttribute("groups")));
        addStringAttributesToView(model);
        return "groups/by-id";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.debug("Searching group with id={}", id);
        GroupResponseDTO groupDTO = groupMapper.convertToGroupResponseDTO(groupService.find(id));
        model.addAttribute("group", groupDTO);
        log.debug("Show {}", Objects.requireNonNull(model.getAttribute("group")));
        return "groups/show";
    }

    @GetMapping("/new")
    public String newGroup(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("group", new GroupRequestDTO());
        addListAttributesToView(model);
        log.debug("Success adding attributes");
        return "groups/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute("group") @Valid GroupRequestDTO groupDTO,
                         BindingResult bindingResult,
                         Model model) {
        log.debug("Saving {}", groupDTO);
        groupValidator.validate(groupDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            addListAttributesToView(model);
            log.debug("There are errors in bindingResult");
            return "groups/new";
        }
        Group group = groupMapper.convertToGroup(groupDTO);
        groupService.add(group);
        log.debug("Success when saving {}", group);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.debug("Searching for updating group with id={}", id);
        GroupRequestDTO groupDTO = groupMapper.convertToGroupRequestDTO(groupService.find(id));
        model.addAttribute("group", groupDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("group")));
        addListAttributesToView(model);
        return "groups/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("group") @Valid GroupRequestDTO groupDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") int id,
                         Model model) {
        log.debug("Updating group with id={} to {}", id, groupDTO);
        groupDTO.setId(id);
        groupValidator.validate(groupDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            addListAttributesToView(model);
            return "groups/edit";
        }
        Group updatedGroup = groupMapper.convertToGroup(groupDTO);
        groupService.update(id, updatedGroup);
        log.debug("Success when updating {}", updatedGroup);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing group with id={}", id);
        groupService.delete(id);
        log.debug("Success when removing group with id={}", id);
        return "redirect:/groups";
    }

    private void addStringAttributesToView(Model model) {
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
    }

    private void addListAttributesToView(Model model) {
        log.debug("Success adding list attributes");
        model.addAttribute("departments", departmentMapper.getDepartmentsDTO());
        model.addAttribute("subjects", subjectMapper.getSubjectsDTO());
        model.addAttribute("lectures", lectureMapper.getLecturesDTO());
        log.debug("Success adding list attributes");
    }
}
