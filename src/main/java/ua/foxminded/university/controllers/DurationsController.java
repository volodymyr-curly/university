package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.Duration;
import ua.foxminded.university.dto.duration.DurationResponseDTO;
import ua.foxminded.university.dto.duration.DurationRequestDTO;
import ua.foxminded.university.service.interfaces.DurationService;
import ua.foxminded.university.util.mappers.DurationMapper;
import ua.foxminded.university.util.validators.DurationValidator;
import ua.foxminded.university.util.validators.interfaces.Persist;

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/durations")
public class DurationsController {

    private static final String TITLE = "Durations";
    private static final String BUTTON = "New duration";
    private static final String CREATE_URL = "durations/new";

    private final DurationService durationService;
    private final DurationMapper durationMapper;
    private final DurationValidator durationValidator;

    @Autowired
    public DurationsController(DurationService durationService,
                               DurationMapper durationMapper,
                               DurationValidator durationValidator) {
        this.durationService = durationService;
        this.durationMapper = durationMapper;
        this.durationValidator = durationValidator;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all durations");
        model.addAttribute("durations", durationMapper.getDurationsDTO());
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("durations")));
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
        return "durations/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.debug("Searching duration with id={}", id);
        DurationResponseDTO durationDetailedDTO = durationMapper.convertToDurationResponseDTO(durationService.find(id));
        model.addAttribute("duration", durationDetailedDTO);
        log.debug("Show {}", Objects.requireNonNull(model.getAttribute("duration")));
        return "durations/show";
    }

    @GetMapping("/new")
    public String newDuration(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("duration", new DurationRequestDTO());
        log.debug("Success adding attributes");
        return "durations/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("duration") @Validated(Persist.class) DurationRequestDTO durationDTO,
                         BindingResult bindingResult) {
        log.debug("Saving {}", durationDTO);
        durationValidator.validate(durationDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "durations/new";
        }
        Duration duration = durationMapper.convertToDuration(durationDTO);
        durationService.add(duration);
        log.debug("Success when save {}", duration);
        return "redirect:/durations";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.debug("Searching for updating duration with id={}", id);
        DurationRequestDTO durationDTO = durationMapper.convertToDurationRequestDTO(durationService.find(id));
        model.addAttribute("duration", durationDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("duration")));
        return "durations/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("duration") @Validated(Persist.class) DurationRequestDTO durationDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        log.debug("Updating duration with id={} to {}", id, durationDTO);
        durationDTO.setId(id);
        durationValidator.validate(durationDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "durations/edit";
        }

        Duration updatedDuration = durationMapper.convertToDuration(durationDTO);
        durationService.update(id, updatedDuration);
        log.debug("Success when save {}", updatedDuration);
        return "redirect:/durations";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing duration with id={}", id);
        durationService.delete(id);
        log.debug("Success when removing duration with id={}", id);
        return "redirect:/durations";
    }
}
