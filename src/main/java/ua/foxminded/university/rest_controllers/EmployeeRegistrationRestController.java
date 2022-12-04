package ua.foxminded.university.rest_controllers;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.domain.ErrorResponse;
import ua.foxminded.university.dto.employee.EmployeeDTO;
import ua.foxminded.university.service.interfaces.EmployeeService;
import ua.foxminded.university.util.mappers.EmployeeMapper;
import ua.foxminded.university.util.validators.PersonValidator;

import javax.validation.Valid;

@Slf4j
@Tag(name = "Employee's registration", description = "The Employee's registration API")
@RestController
@RequestMapping("/api/employee")
public class EmployeeRegistrationRestController extends DefaultController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final PersonValidator personValidator;

    public EmployeeRegistrationRestController(EmployeeService employeeService,
                                              EmployeeMapper employeeMapper,
                                              PersonValidator personValidator) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.personValidator = personValidator;
    }

    @Operation(summary = "Registers new employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registered new employee",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Employee had validation errors, threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "409", description = "Employee has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Person already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> registerEmployee(@RequestBody @Valid EmployeeDTO employeeDTO,
                                                       BindingResult bindingResult) {
        log.debug("Register {}", employeeDTO);
        personValidator.validate(employeeDTO, bindingResult);
        checkForErrors(bindingResult);
        Employee employee = employeeMapper.convertToEmployee(employeeDTO);
        employeeService.add(employee);
        log.trace("Success when register {}", employee);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
