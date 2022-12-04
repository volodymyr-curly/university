package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.LectureRoom;
import ua.foxminded.university.dto.room.RoomResponseDTO;
import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.service.interfaces.LectureRoomService;
import ua.foxminded.university.util.mappers.RoomMapper;
import ua.foxminded.university.util.validators.RoomValidator;
import ua.foxminded.university.util.validators.interfaces.Persist;

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/rooms")
public class LectureRoomsController {

    private static final String TITLE = "Lecture rooms";
    private static final String BUTTON = "New lecture room";
    private static final String CREATE_URL = "rooms/new";

    private final LectureRoomService roomService;
    private final RoomMapper roomMapper;
    private final RoomValidator roomValidator;

    @Autowired
    public LectureRoomsController(LectureRoomService roomService,
                                  RoomMapper roomMapper,
                                  RoomValidator roomValidator) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
        this.roomValidator = roomValidator;
    }

    @GetMapping()
    public String showAll(Model model) {
        log.debug("Searching to show all rooms");
        model.addAttribute("rooms", roomMapper.getRoomsDTO());
        log.debug("Show all {}", Objects.requireNonNull(model.getAttribute("rooms")));
        model.addAttribute("title", TITLE);
        model.addAttribute("button", BUTTON);
        model.addAttribute("createURl", CREATE_URL);
        return "rooms/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.debug("Searching room with id={}", id);
        RoomResponseDTO roomDTO = roomMapper.convertToRoomResponseDTO(roomService.find(id));
        model.addAttribute("room", roomDTO);
        log.debug("Show {}", Objects.requireNonNull(model.getAttribute("room")));
        return "rooms/show";
    }

    @GetMapping("/new")
    public String newRoom(Model model) {
        log.debug("Add attributes then save");
        model.addAttribute("room", new RoomRequestDTO());
        log.debug("Success adding attributes");
        return "rooms/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("room") @Validated(Persist.class) RoomRequestDTO roomDTO,
                         BindingResult bindingResult) {
        log.debug("Saving {}", roomDTO);
        roomValidator.validate(roomDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "rooms/new";
        }

        LectureRoom room = roomMapper.convertToRoom(roomDTO);
        roomService.add(room);
        log.debug("Success when saving {}", room);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.debug("Searching for updating room with id={}", id);
        RoomRequestDTO roomDTO = roomMapper.convertToRoomRequestDTO(roomService.find(id));
        model.addAttribute("room", roomDTO);
        log.debug("Success when searching {}", Objects.requireNonNull(model.getAttribute("room")));
        return "rooms/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("room") @Validated(Persist.class) RoomRequestDTO roomDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        log.debug("Updating room with id={} to {}", id, roomDTO);
        roomDTO.setId(id);
        roomValidator.validate(roomDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("There are errors in bindingResult");
            return "rooms/edit";
        }

        LectureRoom updatedRoom = roomMapper.convertToRoom(roomDTO);
        roomService.update(id, updatedRoom);
        log.debug("Success when updating {}", updatedRoom);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.debug("Removing room with id={}", id);
        roomService.delete(id);
        log.debug("Success when removing room with id={}", id);
        return "redirect:/rooms";
    }
}
