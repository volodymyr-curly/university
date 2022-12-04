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
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.dto.student.StudentRequestDTO;
import ua.foxminded.university.dto.student.StudentResponseDTO;
import ua.foxminded.university.service.interfaces.StudentService;
import ua.foxminded.university.util.mappers.StudentMapper;
import ua.foxminded.university.util.validators.PersonValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Student", description = "The Student API")
@RestController
@RequestMapping("/api/students")
public class StudentsRestController extends DefaultController {

    private final StudentService studentService;
    private final PersonValidator personValidator;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentsRestController(StudentService studentService,
                                  PersonValidator personValidator,
                                  StudentMapper studentMapper) {
        this.studentService = studentService;
        this.personValidator = personValidator;
        this.studentMapper = studentMapper;
    }

    @Operation(summary = "Gets all students")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all students",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = StudentResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Students not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<StudentResponseDTO> showAll() {
        log.debug("Searching to show all students");
        List<StudentResponseDTO> studentsDTO = studentService.findAll().stream()
            .map(studentMapper::convertToDTOWhenFindAll)
            .toList();
        log.trace("Show all {}", studentsDTO);
        return studentsDTO;
    }

    @Operation(summary = "Gets students list by group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found students list by group",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = StudentResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Students not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/group/list")
    public List<StudentResponseDTO> showByGroup(@Parameter(description = "group's id to be searched")
                                                @RequestParam("id") int id) {
        log.debug("Searching by group id={}", id);
        List<StudentResponseDTO> studentsDTO = studentService.findByGroup(id).stream()
            .map(studentMapper::convertToDTOWhenFindAll)
            .toList();
        log.trace("Show by group id {}", studentsDTO);
        return studentsDTO;
    }

    @Operation(summary = "Gets student by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found student by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentResponseDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Person not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Person not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}")
    public StudentResponseDTO show(@Parameter(description = "student's id to be searched")
                                   @PathVariable("id") int id) {
        log.debug("Searching student with id={}", id);
        StudentResponseDTO studentDTO = studentMapper.convertToDTOWhenFindOne(studentService.find(id));
        log.trace("Show {}", studentDTO);
        return studentDTO;
    }

    @Operation(summary = "Creates new student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new student",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Student had validation errors, threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "409", description = "Student has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Person already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid StudentRequestDTO studentDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", studentDTO);
        personValidator.validate(studentDTO, bindingResult);
        checkForErrors(bindingResult);
        Student student = studentMapper.convertToStudent(studentDTO);
        studentService.add(student);
        log.trace("Success when saving {}", student);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets student by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found student by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Person not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Person not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public StudentRequestDTO edit(@Parameter(description = "student's id to be searched to update")
                                  @PathVariable("id") int id) {
        log.debug("Searching for updating student with id={}", id);
        StudentRequestDTO studentDTO = studentMapper.convertToStudentRequestDTO(studentService.find(id));
        log.trace("Success when searching {}", studentDTO);
        return studentDTO;
    }

    @Operation(summary = "Updates student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated student",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Student had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Person not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Person not found"))),
        @ApiResponse(responseCode = "409", description = "Student has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Person already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid StudentRequestDTO studentDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "student's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating student with id={} to {}", id, studentDTO);
        studentDTO.setId(id);
        personValidator.validate(studentDTO, bindingResult);
        checkForErrors(bindingResult);
        Student updatedStudent = studentMapper.convertToStudent(studentDTO);
        studentService.update(id, updatedStudent);
        log.trace("Success when updating {}", updatedStudent);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes student by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed student by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Person not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Person not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "student's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing student with id={}", id);
        studentService.delete(id);
        log.trace("Success when removing student with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
