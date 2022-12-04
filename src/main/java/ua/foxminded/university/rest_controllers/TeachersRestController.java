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
import ua.foxminded.university.domain.Teacher;
import ua.foxminded.university.dto.teacher.TeacherRequestDTO;
import ua.foxminded.university.dto.teacher.TeacherResponseDTO;
import ua.foxminded.university.service.interfaces.TeacherService;
import ua.foxminded.university.util.mappers.TeacherMapper;
import ua.foxminded.university.util.validators.TeacherValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Teacher", description = "The Teacher API")
@RestController
@RequestMapping("/api/teachers")
public class TeachersRestController extends DefaultController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;
    private final TeacherValidator teacherValidator;

    @Autowired
    public TeachersRestController(TeacherService teacherService,
                                  TeacherMapper teacherMapper,
                                  TeacherValidator teacherValidator) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
        this.teacherValidator = teacherValidator;
    }

    @Operation(summary = "Gets all teachers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all teachers",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TeacherResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Teachers not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<TeacherRequestDTO> showAll() {
        log.debug("Searching to show all teachers");
        List<TeacherRequestDTO> teachersDTO = teacherService.findAll().stream()
            .map(teacherMapper::convertToTeacherDTO)
            .toList();
        log.trace("Show all {}", teachersDTO);
        return teachersDTO;
    }

    @Operation(summary = "Gets teachers list by department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found teachers list by department",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TeacherResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Teachers not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/department/list")
    public List<TeacherRequestDTO> showByDepartment(@Parameter(description = "department's id to be searched")
                                                    @RequestParam("id") int id) {
        log.debug("Searching by department id={}", id);
        List<TeacherRequestDTO> teachersDTO = teacherService.findByDepartment(id).stream()
            .map(teacherMapper::convertToTeacherDTO)
            .toList();
        log.trace("Show all {}", teachersDTO);
        return teachersDTO;
    }

    @Operation(summary = "Gets teachers list by subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found teachers list by subject",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TeacherResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Teachers not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/subject/list")
    public List<TeacherRequestDTO> showBySubject(@Parameter(description = "subject's id to be searched")
                                                 @RequestParam("id") int id) {
        log.debug("Searching by subject id={}", id);
        List<TeacherRequestDTO> teachersDTO = teacherService.findBySubject(id).stream()
            .map(teacherMapper::convertToTeacherDTO)
            .toList();
        log.trace("Show by subject id {}", teachersDTO);
        return teachersDTO;
    }

    @Operation(summary = "Gets teacher by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found teacher by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = TeacherResponseDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Teacher not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Teacher not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}")
    public TeacherResponseDTO show(@Parameter(description = "teacher's id to be searched")
                                   @PathVariable("id") int id) {
        log.debug("Searching teacher with id={}", id);
        TeacherResponseDTO teacherDTO = teacherMapper
            .convertToTeacherResponseDTO(teacherService.find(id));
        log.trace("Show {}", teacherDTO);
        return teacherDTO;
    }

    @Operation(summary = "Creates new teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new teacher",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Teacher had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TeacherRequestDTO teacherDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", teacherDTO);
        teacherValidator.validate(teacherDTO, bindingResult);
        checkForErrors(bindingResult);
        Teacher teacher = teacherMapper.convertToTeacher(teacherDTO);
        teacherService.add(teacher);
        log.trace("Success when saving {}", teacher);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets teacher by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found teacher by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = TeacherRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Teacher not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Teacher not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public TeacherRequestDTO edit(@Parameter(description = "teacher's id to be searched to update")
                                  @PathVariable("id") int id) {
        log.debug("Searching for updating teacher with id={}", id);
        TeacherRequestDTO teacherDTO = teacherMapper.convertToTeacherDTO(teacherService.find(id));
        log.trace("Success when searching {}", teacherDTO);
        return teacherDTO;
    }

    @Operation(summary = "Updates teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated teacher",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Teacher had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Teacher not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Teacher not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid TeacherRequestDTO teacherDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "teacher's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating teacher with id={} to {}", id, teacherDTO);
        teacherDTO.setId(id);
        teacherValidator.validate(teacherDTO, bindingResult);
        checkForErrors(bindingResult);
        Teacher updatedTeacher = teacherMapper.convertToTeacher(teacherDTO);
        teacherService.update(id, updatedTeacher);
        log.trace("Success when updating {}", updatedTeacher);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes teacher by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed teacher by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Teacher not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Teacher not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "teacher's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing teacher with id={}", id);
        teacherService.delete(id);
        log.trace("Success when removing teacher with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
