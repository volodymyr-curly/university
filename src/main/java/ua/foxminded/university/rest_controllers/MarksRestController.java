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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.university.domain.ErrorResponse;
import ua.foxminded.university.domain.Mark;
import ua.foxminded.university.dto.mark.MarkRequestDTO;
import ua.foxminded.university.dto.mark.MarkResponseDTO;
import ua.foxminded.university.service.interfaces.MarkService;
import ua.foxminded.university.util.mappers.MarkMapper;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Mark", description = "The Mark API")
@RestController
@RequestMapping("/api/marks")
public class MarksRestController extends DefaultController {

    private final MarkService markService;
    private final MarkMapper markMapper;

    public MarksRestController(MarkService markService, MarkMapper markMapper) {
        this.markService = markService;
        this.markMapper = markMapper;
    }

    @Operation(summary = "Gets all marks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all marks",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = MarkResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Marks not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<MarkResponseDTO> showAll() {
        log.debug("Searching to show all marks");
        List<MarkResponseDTO> marksDTO = markService.findAll().stream()
            .map(markMapper::convertToMarkResponseDTO)
            .toList();
        log.trace("Show all {}", marksDTO);
        return marksDTO;
    }

    @Operation(summary = "Gets marks list by subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found marks list by subject",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = MarkResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Marks not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/subject/list")
    public List<MarkResponseDTO> showBySubject(@Parameter(description = "subject's id to be searched")
                                               @RequestParam("id") int id) {
        log.debug("Searching by subject id={}", id);
        List<MarkResponseDTO> marksDTO = markService.findBySubject(id).stream()
            .map(markMapper::convertToMarkResponseDTO)
            .toList();
        log.trace("Show by subject id {}", marksDTO);
        return marksDTO;
    }

    @Operation(summary = "Gets marks list by student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found marks list by student",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = MarkResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Marks not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/student/list")
    public List<MarkResponseDTO> showByStudent(@Parameter(description = "student's id to be searched")
                                               @RequestParam("id") int id) {
        log.debug("Searching by student id={}", id);
        List<MarkResponseDTO> marksDTO = markService.findByStudent(id).stream()
            .map(markMapper::convertToMarkResponseDTO)
            .toList();
        log.trace("Show by student id {}", marksDTO);
        return marksDTO;
    }

    @Operation(summary = "Creates new mark")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new mark",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Mark had validation errors, threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "409", description = "Mark has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Mark already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MarkRequestDTO markDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", markDTO);
        checkForErrors(bindingResult);
        Mark mark = markMapper.convertToMark(markDTO);
        markService.add(mark);
        log.trace("Success when saving {}", mark);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets mark by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found mark by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = MarkRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Mark not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Mark not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public MarkRequestDTO edit(@Parameter(description = "mark's id to be searched to update")
                               @PathVariable("id") int id) {
        log.debug("Searching for updating mark with id={}", id);
        MarkRequestDTO markDTO = markMapper.convertToMarkRequestDTO(markService.find(id));
        log.trace("Success when searching {}", markDTO);
        return markDTO;
    }

    @Operation(summary = "Updates mark")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated mark",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Mark had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Mark not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Mark not found"))),
        @ApiResponse(responseCode = "409", description = "Mark has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Mark already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid MarkRequestDTO markDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "mark's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating mark with id={} to {}", id, markDTO);
        markDTO.setId(id);
        checkForErrors(bindingResult);
        Mark updatedMark = markMapper.convertToMark(markDTO);
        markService.update(id, updatedMark);
        log.trace("Success when updating {}", updatedMark);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes mark by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed mark by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Mark not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Mark not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "mark's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing mark with id={}", id);
        markService.delete(id);
        log.trace("Success when removing mark with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
