package ua.foxminded.university.rest_controllers;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.university.domain.ErrorResponse;
import ua.foxminded.university.domain.Student;
import ua.foxminded.university.dto.student.StudentRequestDTO;
import ua.foxminded.university.service.interfaces.StudentService;
import ua.foxminded.university.util.mappers.StudentMapper;
import ua.foxminded.university.util.validators.PersonValidator;

import javax.validation.Valid;

@Slf4j
@Tag(name = "Student's registration", description = "The Student's registration API")
@RestController
@RequestMapping("/api/student")
public class StudentRegistrationRestController extends DefaultController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final PersonValidator personValidator;

    @Autowired
    public StudentRegistrationRestController(StudentService studentService,
                                             StudentMapper studentMapper,
                                             PersonValidator personValidator) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.personValidator = personValidator;
    }

    @Operation(summary = "Registers new student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registered new student",
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
    public ResponseEntity<HttpStatus> registerStudent(@RequestBody @Valid StudentRequestDTO studentDTO,
                                                      BindingResult bindingResult) {
        log.debug("Register {}", studentDTO);
        personValidator.validate(studentDTO, bindingResult);
        checkForErrors(bindingResult);
        Student student = studentMapper.convertToStudent(studentDTO);
        studentService.add(student);
        log.trace("Success when register {}", student);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
