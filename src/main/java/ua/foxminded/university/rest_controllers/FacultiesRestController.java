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
import ua.foxminded.university.domain.Faculty;
import ua.foxminded.university.dto.faculty.FacultyRequestDTO;
import ua.foxminded.university.dto.faculty.FacultyResponseDTO;
import ua.foxminded.university.service.interfaces.FacultyService;
import ua.foxminded.university.util.mappers.FacultyMapper;
import ua.foxminded.university.util.validators.FacultyValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Faculty", description = "The Faculty API")
@RestController
@RequestMapping("/api/faculties")
public class FacultiesRestController extends DefaultController {

    private final FacultyService facultyService;
    private final FacultyMapper facultyMapper;
    private final FacultyValidator facultyValidator;

    @Autowired
    public FacultiesRestController(FacultyService facultyService,
                                   FacultyMapper facultyMapper,
                                   FacultyValidator facultyValidator) {
        this.facultyService = facultyService;
        this.facultyMapper = facultyMapper;
        this.facultyValidator = facultyValidator;
    }

    @Operation(summary = "Gets all faculties")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all faculties",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = FacultyResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Faculties not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<FacultyResponseDTO> showAll() {
        log.debug("Searching to show all faculties");
        List<FacultyResponseDTO> facultiesDTO = facultyMapper.getFacultiesDTO();
        log.trace("Show all {}", facultiesDTO);
        return facultiesDTO;
    }

    @Operation(summary = "Creates new faculty")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new faculty",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Faculty had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid FacultyRequestDTO facultyDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", facultyDTO);
        facultyValidator.validate(facultyDTO, bindingResult);
        checkForErrors(bindingResult);
        Faculty faculty = facultyMapper.convertToFaculty(facultyDTO);
        facultyService.add(faculty);
        log.trace("Success when saving {}", faculty);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets faculty by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found faculty by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = FacultyRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Faculty not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Faculty not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public FacultyRequestDTO edit(@Parameter(description = "faculty's id to be searched to update")
                                  @PathVariable("id") int id) {
        log.debug("Searching for updating faculty with id={}", id);
        FacultyRequestDTO facultyDTO = facultyMapper.convertToFacultyRequestDTO(facultyService.find(id));
        log.trace("Success when searching {}", facultyDTO);
        return facultyDTO;
    }

    @Operation(summary = "Updates faculty")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated faculty",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Faculty had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Faculty not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Faculty not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid FacultyRequestDTO facultyDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "faculty's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating faculty with id={} to {}", id, facultyDTO);
        facultyDTO.setId(id);
        facultyValidator.validate(facultyDTO, bindingResult);
        checkForErrors(bindingResult);
        Faculty updatedFaculty = facultyMapper.convertToFaculty(facultyDTO);
        facultyService.update(id, updatedFaculty);
        log.trace("Success when updating {}", updatedFaculty);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes faculty by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed faculty by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Faculty not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Faculty not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "faculty's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing faculty with id={}", id);
        facultyService.delete(id);
        log.trace("Success when removing faculty with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
