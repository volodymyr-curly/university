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
import ua.foxminded.university.domain.Lecture;
import ua.foxminded.university.dto.lecture.LectureRequestDTO;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.service.interfaces.LectureService;
import ua.foxminded.university.util.mappers.LectureMapper;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name="Lecture", description = "The Lecture API")
@RestController
@RequestMapping("/api/lectures")
public class LecturesRestController extends DefaultController {

    private final LectureService lectureService;
    private final LectureMapper lectureMapper;

    @Autowired
    public LecturesRestController(LectureService lectureService, LectureMapper lectureMapper) {
        this.lectureService = lectureService;
        this.lectureMapper = lectureMapper;
    }

    @Operation(summary = "Gets all lectures")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all lectures",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = LectureResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Lectures not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<LectureResponseDTO> showAll() {
        log.debug("Searching to show all lectures");
        List<LectureResponseDTO> lecturesDTO = lectureService.findAll().stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        log.trace("Show all {}", lecturesDTO);
        return lecturesDTO;
    }

    @Operation(summary = "Gets lectures list by subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found lectures list by subject",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = LectureResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Lectures not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/subject/list")
    public List<LectureResponseDTO> showBySubject(@Parameter(description = "subject's id to be searched")
                                                      @RequestParam("id") int id) {
        log.debug("Searching by subject id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findBySubject(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        log.trace("Show by subject id {}", lecturesDTO);
        return lecturesDTO;
    }

    @Operation(summary = "Gets lectures list by teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found lectures list by teacher",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = LectureResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Lectures not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/teacher/list")
    public List<LectureResponseDTO> showByTeacher(@Parameter(description = "teacher's id to be searched")
                                                      @RequestParam("id") int id) {
        log.debug("Searching by teacher id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findByTeacher(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        log.trace("Show by teacher id {}", lecturesDTO);
        return lecturesDTO;
    }

    @Operation(summary = "Gets lectures list by group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found lectures list by group",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = LectureResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Lectures not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/group/list")
    public List<LectureResponseDTO> showByGroup(@Parameter(description = "group's id to be searched")
                                                    @RequestParam("id") int id) {
        log.debug("Searching by group id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findByGroup(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        log.trace("Show by group id {}", lecturesDTO);
        return lecturesDTO;
    }

    @Operation(summary = "Gets lectures list by room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found lectures list by room",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = LectureResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Lectures not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/room/list")
    public List<LectureResponseDTO> showByRoom(@Parameter(description = "room's id to be searched")
                                                   @RequestParam("id") int id) {
        log.debug("Searching by room id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findByRoom(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        log.trace("Show by room id {}", lecturesDTO);
        return lecturesDTO;
    }

    @Operation(summary = "Gets lectures list by duration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found lectures list by duration",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = LectureResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Lectures not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/duration/list")
    public List<LectureResponseDTO> showByDuration(@Parameter(description = "duration's id to be searched")
                                                       @RequestParam("id") int id) {
        log.debug("Searching by duration id={}", id);
        List<LectureResponseDTO> lecturesDTO = lectureService.findByDuration(id).stream()
            .map(lectureMapper::convertToLectureResponseDTO)
            .toList();
        log.trace("Show by duration id {}", lecturesDTO);
        return lecturesDTO;
    }

    @Operation(summary = "Creates new lecture")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new lecture",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Lecture had validation errors, threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "409", description = "Lecture has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Lecture already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid LectureRequestDTO lectureDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", lectureDTO);
        checkForErrors(bindingResult);
        Lecture lecture = lectureMapper.convertToLecture(lectureDTO);
        lectureService.add(lecture);
        log.trace("Success when saving {}", lecture);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets lecture by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found lecture by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = LectureRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Lecture not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Lecture not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public LectureRequestDTO edit(@Parameter(description = "lecture's id to be searched to update")
                                      @PathVariable("id") int id) {
        log.debug("Searching for updating lecture with id={}", id);
        LectureRequestDTO lectureDTO = lectureMapper
            .convertToLectureRequestDTO(lectureService.find(id));
        log.trace("Success when searching {}", lectureDTO);
        return lectureDTO;
    }

    @Operation(summary = "Updates lecture")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated lecture",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Lecture had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Lecture not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Lecture not found"))),
        @ApiResponse(responseCode = "409", description = "Lecture has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Lecture already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid LectureRequestDTO lectureDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "lecture's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating lecture with id={} to {}", id, lectureDTO);
        lectureDTO.setId(id);
        checkForErrors(bindingResult);
        Lecture updatedLecture = lectureMapper.convertToLecture(lectureDTO);
        lectureService.update(id, updatedLecture);
        log.trace("Success when updating {}", updatedLecture);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes lecture by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed lecture by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Lecture not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Lecture not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "lecture's id to be removed")
                                                 @PathVariable("id") int id) {
        log.debug("Removing lecture with id={}", id);
        lectureService.delete(id);
        log.trace("Success when removing lecture with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
