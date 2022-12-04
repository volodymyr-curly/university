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
import ua.foxminded.university.domain.Subject;
import ua.foxminded.university.dto.subject.SubjectRequestDTO;
import ua.foxminded.university.dto.subject.SubjectResponseDTO;
import ua.foxminded.university.service.interfaces.SubjectService;
import ua.foxminded.university.util.mappers.SubjectMapper;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Subject", description = "The Subject API")
@RestController
@RequestMapping("/api/subjects")
public class SubjectsRestController extends DefaultController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    @Autowired
    public SubjectsRestController(SubjectService subjectService,
                                  SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    @Operation(summary = "Gets all subjects")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all subjects",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = SubjectResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Subjects not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<SubjectResponseDTO> showAll() {
        log.debug("Searching to show all subjects");
        List<SubjectResponseDTO> subjectsDTO = subjectService.findAll().stream()
            .map(subjectMapper::convertToSubjectResponseDTOWhenFindAll)
            .toList();
        log.trace("Show all {}", subjectsDTO);
        return subjectsDTO;
    }

    @Operation(summary = "Gets subjects list by group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found subjects list by group",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = SubjectResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Subjects not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/group/list")
    public List<SubjectResponseDTO> showByGroup(@Parameter(description = "group's id to be searched")
                                                @RequestParam("id") int id) {
        log.debug("Searching by group id={}", id);
        List<SubjectResponseDTO> subjectsDTO = subjectService.findByGroup(id).stream()
            .map(subjectMapper::convertToSubjectResponseDTOWhenFindAll)
            .toList();
        log.trace("Show by group id {}", subjectsDTO);
        return subjectsDTO;
    }

    @Operation(summary = "Gets subjects list by teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found subjects list by teacher",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = SubjectResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Subjects not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/teacher/list")
    public List<SubjectResponseDTO> showByTeacher(@Parameter(description = "teacher's id to be searched")
                                                  @RequestParam("id") int id) {
        log.debug("Searching by teacher id={}", id);
        List<SubjectResponseDTO> subjectsDTO = subjectService.findByGroup(id).stream()
            .map(subjectMapper::convertToSubjectResponseDTOWhenFindAll)
            .toList();
        log.trace("Show by teacher id {}", subjectsDTO);
        return subjectsDTO;
    }

    @Operation(summary = "Gets subject by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found subject by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = SubjectResponseDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Subject not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Subject not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}")
    public SubjectResponseDTO show(@Parameter(description = "subject's id to be searched")
                                   @PathVariable("id") int id) {
        log.debug("Searching subject with id={}", id);
        SubjectResponseDTO subjectDTO = subjectMapper
            .convertToSubjectResponseDTO(subjectService.find(id));
        log.trace("Show {}", subjectDTO);
        return subjectDTO;
    }

    @Operation(summary = "Creates new subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new subject",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Subject had validation errors, threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "409", description = "Subject has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Subject already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SubjectRequestDTO subjectDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", subjectDTO);
        checkForErrors(bindingResult);
        Subject subject = subjectMapper.convertToSubject(subjectDTO);
        subjectService.add(subject);
        log.trace("Success when saving {}", subject);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets subject by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found subject by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = SubjectRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Subject not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Subject not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public SubjectRequestDTO edit(@Parameter(description = "subject's id to be searched to update")
                                  @PathVariable("id") int id) {
        log.debug("Searching for updating subject with id={}", id);
        SubjectRequestDTO subjectDTO = subjectMapper.convertToSubjectRequestDTO(subjectService.find(id));
        log.trace("Success when searching {}", subjectDTO);
        return subjectDTO;
    }

    @Operation(summary = "Updates subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated subject",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Subject had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Subject not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Subject not found"))),
        @ApiResponse(responseCode = "409", description = "Subject has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Subject already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid SubjectRequestDTO subjectDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "subject's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating subject with id={} to {}", id, subjectDTO);
        subjectDTO.setId(id);
        checkForErrors(bindingResult);
        Subject updatedSubject = subjectMapper.convertToSubject(subjectDTO);
        subjectService.update(id, updatedSubject);
        log.trace("Success when updating {}", updatedSubject);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes subject by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed subject by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Subject not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Subject not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "subject's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing subject with id={}", id);
        subjectService.delete(id);
        log.trace("Success when removing subject with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
