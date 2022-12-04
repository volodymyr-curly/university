package ua.foxminded.university.rest_controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.ErrorResponse;
import ua.foxminded.university.domain.LectureRoom;
import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.dto.room.RoomResponseDTO;
import ua.foxminded.university.service.interfaces.LectureRoomService;
import ua.foxminded.university.util.mappers.RoomMapper;
import ua.foxminded.university.util.validators.RoomValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Lecture Room", description = "The Lecture Room API")
@RestController
@RequestMapping("/api/rooms")
public class LectureRoomsRestController extends DefaultController {

    private final LectureRoomService roomService;
    private final RoomMapper roomMapper;
    private final RoomValidator roomValidator;

    @Autowired
    public LectureRoomsRestController(LectureRoomService roomService,
                                      RoomMapper roomMapper,
                                      RoomValidator roomValidator) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
        this.roomValidator = roomValidator;
    }

    @Operation(summary = "Gets all rooms")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all rooms",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RoomRequestDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Rooms not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<RoomRequestDTO> showAll() {
        log.debug("Searching to show all rooms");
        List<RoomRequestDTO> roomsDTO = roomMapper.getRoomsDTO();
        log.trace("Show all {}", roomsDTO);
        return roomsDTO;
    }

    @Operation(summary = "Gets room by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found room by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = RoomResponseDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Room not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Room not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}")
    public RoomResponseDTO show(@Parameter(description = "room's id to be searched")
                                @PathVariable("id") int id) {
        log.debug("Searching room with id={}", id);
        RoomResponseDTO roomDTO = roomMapper.convertToRoomResponseDTO(roomService.find(id));
        log.trace("Show {}", roomDTO);
        return roomDTO;
    }

    @Operation(summary = "Creates new room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new room",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Room had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid RoomRequestDTO roomDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", roomDTO);
        roomValidator.validate(roomDTO, bindingResult);
        checkForErrors(bindingResult);
        LectureRoom room = roomMapper.convertToRoom(roomDTO);
        roomService.add(room);
        log.trace("Success when saving {}", room);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets room by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found room by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = RoomRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Room not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Room not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public RoomRequestDTO edit(@Parameter(description = "room's id to be searched to update")
                               @PathVariable("id") int id) {
        log.debug("Searching for updating room with id={}", id);
        RoomRequestDTO roomDTO = roomMapper.convertToRoomRequestDTO(roomService.find(id));
        log.trace("Success when searching {}", roomDTO);
        return roomDTO;
    }

    @Operation(summary = "Updates room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated room",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Room had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Room not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Room not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid RoomRequestDTO roomDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "room's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating room with id={} to {}", id, roomDTO);
        roomDTO.setId(id);
        roomValidator.validate(roomDTO, bindingResult);
        checkForErrors(bindingResult);
        LectureRoom updatedRoom = roomMapper.convertToRoom(roomDTO);
        roomService.update(id, updatedRoom);
        log.trace("Success when updating {}", updatedRoom);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes room by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed room by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Room not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Room not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "room's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing room with id={}", id);
        roomService.delete(id);
        log.trace("Success when removing room with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
