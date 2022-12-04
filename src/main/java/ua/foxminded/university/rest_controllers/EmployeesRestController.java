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
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.domain.ErrorResponse;
import ua.foxminded.university.dto.employee.EmployeeDTO;
import ua.foxminded.university.service.interfaces.EmployeeService;
import ua.foxminded.university.util.mappers.EmployeeMapper;
import ua.foxminded.university.util.validators.PersonValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Employee", description = "The Employee API")
@RestController
@RequestMapping("/api/employees")
public class EmployeesRestController extends DefaultController {

    private final EmployeeService employeeService;
    private final PersonValidator personValidator;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeesRestController(EmployeeService employeeService,
                                   PersonValidator personValidator,
                                   EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.personValidator = personValidator;
        this.employeeMapper = employeeMapper;
    }

    @Operation(summary = "Gets all employees")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all employees",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Employees not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<EmployeeDTO> showAll() {
        log.debug("Searching to show all employees");
        List<EmployeeDTO> employeesDTO = employeeService.findAll().stream()
            .map(employeeMapper::convertToDTOWhenFindAll)
            .toList();
        log.trace("Show all {}", employeesDTO);
        return employeesDTO;
    }

    @Operation(summary = "Gets all employees who aren't teachers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all employees who aren't teachers",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Employees not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/not-teachers/list")
    public List<EmployeeDTO> showNoTeachers() {
        log.debug("Searching to show employees are not teachers");
        List<EmployeeDTO> employeesDTO = employeeService.findNotTeachers().stream()
            .map(employeeMapper::convertToDTOWhenFindAll)
            .toList();
        log.trace("Show not teachers {}", employeesDTO);
        return employeesDTO;
    }

    @Operation(summary = "Gets employees list by department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found employees list by department",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Employees not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/department/list")
    public List<EmployeeDTO> showByDepartment(@Parameter(description = "department's id to be searched")
                                              @RequestParam("id") int id) {
        log.debug("Searching by department id={}", id);
        List<EmployeeDTO> employeesDTO = employeeService.findByDepartment(id).stream()
            .map(employeeMapper::convertToDTOWhenFindAll)
            .toList();
        log.trace("Show by department id {}", employeesDTO);
        return employeesDTO;
    }

    @Operation(summary = "Gets employee by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found employee by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = EmployeeDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Person not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Person not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}")
    public EmployeeDTO show(@Parameter(description = "employee's id to be searched")
                            @PathVariable("id") int id) {
        log.debug("Searching employee with id={}", id);
        EmployeeDTO employeeDTO = employeeMapper.convertToDTOWhenFindOne(employeeService.find(id));
        log.trace("Show {}", employeeDTO);
        return employeeDTO;
    }

    @Operation(summary = "Creates new employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new employee",
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
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid EmployeeDTO employeeDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", employeeDTO);
        personValidator.validate(employeeDTO, bindingResult);
        checkForErrors(bindingResult);
        Employee employee = employeeMapper.convertToEmployee(employeeDTO);
        employeeService.add(employee);
        log.trace("Success when saving {}", employee);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets employee by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found employee by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = EmployeeDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Person not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Person not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public EmployeeDTO edit(@Parameter(description = "employee's id to be searched to update")
                            @PathVariable("id") int id) {
        log.debug("Searching for updating employee with id={}", id);
        EmployeeDTO employeeDTO = employeeMapper.convertToDTOWhenFindOne(employeeService.find(id));
        log.trace("Success when searching {}", employeeDTO);
        return employeeDTO;
    }

    @Operation(summary = "Updates employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated employee",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Employee had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Person not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Person not found"))),
        @ApiResponse(responseCode = "409", description = "Employee has already exist",
            content = {@Content(mediaType = "text/plain",
                schema = @Schema(example = "Person already exists"))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid EmployeeDTO employeeDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "employee's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating employee with id={} to {}", id, employeeDTO);
        employeeDTO.setId(id);
        personValidator.validate(employeeDTO, bindingResult);
        checkForErrors(bindingResult);
        Employee updatedEmployee = employeeMapper.convertToEmployee(employeeDTO);
        employeeService.update(id, updatedEmployee);
        log.trace("Success when updating {}", updatedEmployee);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes employee by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed employee by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Person not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Person not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "employee's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing employee with id={}", id);
        employeeService.delete(id);
        log.trace("Success when removing employee with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
