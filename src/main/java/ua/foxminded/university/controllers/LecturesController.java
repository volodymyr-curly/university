package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Lecture;
import ua.foxminded.university.dto.duration.DurationRequestDTO;
import ua.foxminded.university.dto.group.GroupNestedDTO;
import ua.foxminded.university.dto.lecture.LectureRequestDTO;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;
import ua.foxminded.university.dto.teacher.TeacherNestedDTO;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.service.interfaces.*;
import ua.foxminded.university.util.mappers.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/lectures")
public class LecturesController {

    private static final String TITLE = "Lectures";
    private static final String BUTTON = "New lecture";
    private static final String CREATE_URL = "lectures/new";

    private final LectureService lectureService;
    private final LectureMapper lectureMapper;
    private final SubjectMapper subjectMapper;
    private final TeacherMapper teacherMapper;
    private final RoomMapper roomMapper;
    private final DurationMapper durationMapper;
    private final GroupMapper groupMapper;

    public LecturesController(LectureService lectureService,
                              LectureMapper lectureMapper,
                              SubjectMapper subjectMapper,
                              TeacherMapper teacherMapper,
                              RoomMapper roomMapper,
                              DurationMapper durationMapper,
                              GroupMapper groupMapper) {
        this.lectureService = lectureService;
        this.lectureMapper = lectureMapper;
        this.subjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
        this.roomMapper = roomMapper;
        this.durationMapper = durationMapper;
        this.groupMapper = groupMapper;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all lectures");
        List<LectureResponseDTO> lecturesDTO = lectureService.findAll().stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        model.addAttribute("lectures", lecturesDTO);
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("lectures")));
        addNewObjectsAttributesToView(model);
        addListAttributesToView(model);
        addStringAttributesToView(model);
        return "lectures/all";
    }

    @GetMapping("/subject/list")
    public String showBySubject(@RequestParam("id") int id, Model model) {
        log.debug("Searching by subject id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findBySubject(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        model.addAttribute("lectures", lecturesDTO);
        log.debug("Show by subject id {}", Objects.requireNonNull(model.getAttribute("lectures")));
        addStringAttributesToView(model);
        return "lectures/by-id";
    }

    @GetMapping("/teacher/list")
    public String showByTeacher(@RequestParam("id") int id, Model model) {
        log.debug("Searching by teacher id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findByTeacher(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        model.addAttribute("lectures", lecturesDTO);
        log.debug("Show by teacher id {}", Objects.requireNonNull(model.getAttribute("lectures")));
        addStringAttributesToView(model);
        return "lectures/by-id";
    }

    @GetMapping("/group/list")
    public String showByGroup(@RequestParam("id") int id, Model model) {
        log.debug("Searching by group id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findByGroup(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        model.addAttribute("lectures", lecturesDTO);
        log.debug("Show by group id {}", Objects.requireNonNull(model.getAttribute("lectures")));
        addStringAttributesToView(model);
        return "lectures/by-id";
    }

    @GetMapping("/room/list")
    public String showByRoom(@RequestParam("id") int id, Model model) {
        log.debug("Searching by room id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findByRoom(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        model.addAttribute("lectures", lecturesDTO);
        log.debug("Show by room id {}", Objects.requireNonNull(model.getAttribute("lectures")));
        addStringAttributesToView(model);
        return "lectures/by-id";
    }

    @GetMapping("/duration/list")
    public String showByDuration(@RequestParam("id") int id, Model model) {
        log.debug("Searching by duration id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findByDuration(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        model.addAttribute("lectures", lecturesDTO);
        log.debug("Show by duration id {}", Objects.requireNonNull(model.getAttribute("lectures")));
        addStringAttributesToView(model);
        return "lectures/by-id";
    }

    @GetMapping("/new")
    public String newLecture(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("lecture", new LectureRequestDTO());
        addListAttributesToView(model);
        log.debug("Success adding attributes");
        return "lectures/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute("lecture") @Valid LectureRequestDTO lectureDTO,
                         BindingResult bindingResult,
                         Model model) {
        log.debug("Saving {}", lectureDTO);
        addListAttributesToView(model);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "lectures/new";
        }

        try {
            Lecture lecture = lectureMapper.convertToLecture(lectureDTO);
            lectureService.add(lecture);
            log.debug("Success when saving {}", lecture);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "lectures/new";
        }

        return "redirect:/lectures";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.debug("Searching for updating lecture with id={}", id);
        LectureRequestDTO lectureDTO = lectureMapper
            .convertToLectureRequestDTO(lectureService.find(id));
        model.addAttribute("lecture", lectureDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("lecture")));
        addListAttributesToView(model);
        return "lectures/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("lecture") @Valid LectureRequestDTO lectureDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") int id,
                         Model model) {
        log.debug("Updating lecture with id={} to {}", id, lectureDTO);
        addListAttributesToView(model);
        lectureDTO.setId(id);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");

            return "lectures/edit";
        }

        try {
            Lecture updatedLecture = lectureMapper.convertToLecture(lectureDTO);
            lectureService.update(id, updatedLecture);
            log.debug("Success when updating {}", updatedLecture);

        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("globalError", e.getMessage()));
            return "lectures/edit";
        }

        return "redirect:/lectures";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing lecture with id={}", id);
        lectureService.delete(id);
        log.debug("Success when removing lecture with id={}", id);
        return "redirect:/lectures";
    }

    private void addStringAttributesToView(Model model) {
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
    }

    private void addListAttributesToView(Model model) {
        model.addAttribute("subjects", subjectMapper.getSubjectsDTO());
        model.addAttribute("teachers", teacherMapper.getTeachersDTO());
        model.addAttribute("groups", groupMapper.getGroupsDTO());
        model.addAttribute("rooms", roomMapper.getRoomsDTO());
        model.addAttribute("durations", durationMapper.getDurationsDTO());
    }

    private void addNewObjectsAttributesToView(Model model) {
        log.debug("Success adding list attributes");
        model.addAttribute("subject", new SubjectNestedDTO());
        model.addAttribute("teacher", new TeacherNestedDTO());
        model.addAttribute("room", new RoomRequestDTO());
        model.addAttribute("duration", new DurationRequestDTO());
        model.addAttribute("group", new GroupNestedDTO());
        log.debug("Success adding list attributes");
    }
}
