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
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.domain.ErrorResponse;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.dto.department.DepartmentResponseDTO;
import ua.foxminded.university.service.interfaces.DepartmentService;
import ua.foxminded.university.util.mappers.DepartmentMapper;
import ua.foxminded.university.util.validators.DepartmentValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Department", description = "The Department API")
@RestController
@RequestMapping("/api/departments")
public class DepartmentsRestController extends DefaultController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final DepartmentValidator departmentValidator;

    @Autowired
    public DepartmentsRestController(DepartmentService departmentService,
                                     DepartmentMapper departmentMapper,
                                     DepartmentValidator departmentValidator) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
        this.departmentValidator = departmentValidator;
    }

    @Operation(summary = "Gets all departments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all departments",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = DepartmentResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Departments not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<DepartmentResponseDTO> showAll() {
        log.debug("Searching to show all departments");
        List<DepartmentResponseDTO> departmentsDTO = departmentService.findAll().stream()
            .map(departmentMapper::convertToDepartmentResponseDTO)
            .toList();
        log.trace("Show all {}", departmentsDTO);
        return departmentsDTO;
    }

    @Operation(summary = "Gets departments list by faculty")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found departments list by faculty",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = DepartmentResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Departments not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/faculty/list")
    public List<DepartmentResponseDTO> showByFaculty(@Parameter(description = "faculty's id to be searched")
                                                     @RequestParam("id") int id) {
        log.debug("Searching by faculty id={}", id);
        List<DepartmentResponseDTO> departmentsDTO = departmentService.findByFaculty(id).stream()
            .map(departmentMapper::convertToDepartmentResponseDTO)
            .toList();
        log.trace("Show by faculty id {}", departmentsDTO);
        return departmentsDTO;
    }


    @Operation(summary = "Gets department by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found department by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = DepartmentResponseDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Department not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Department not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}")
    public DepartmentResponseDTO show(@Parameter(description = "department's id to be searched")
                                      @PathVariable("id") int id) {
        log.debug("Searching department with id={}", id);
        DepartmentResponseDTO departmentDTO = departmentMapper
            .convertToDepartmentResponseDTO(departmentService.find(id));
        log.trace("Show {}", departmentDTO);
        return departmentDTO;
    }

    @Operation(summary = "Creates new department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new department",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Department had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid DepartmentRequestDTO departmentDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", departmentDTO);
        departmentValidator.validate(departmentDTO, bindingResult);
        checkForErrors(bindingResult);
        Department department = departmentMapper.convertToDepartment(departmentDTO);
        departmentService.add(department);
        log.trace("Success when saving {}", department);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets department by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found department by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = DepartmentRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Department not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Department not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public DepartmentRequestDTO edit(@Parameter(description = "department's id to be searched")
                                     @PathVariable("id") int id) {
        log.debug("Searching for updating department with id={}", id);
        DepartmentRequestDTO departmentDTO = departmentMapper
            .convertToDepartmentRequestDTO(departmentService.find(id));
        log.trace("Success when searching {}", departmentDTO);
        return departmentDTO;
    }

    @Operation(summary = "Updates department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated department",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Department had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Department not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Department not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid DepartmentRequestDTO departmentDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "department's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating department with id={} to {}", id, departmentDTO);
        departmentDTO.setId(id);
        departmentValidator.validate(departmentDTO, bindingResult);
        checkForErrors(bindingResult);
        Department updatedDepartment = departmentMapper.convertToDepartment(departmentDTO);
        departmentService.update(id, updatedDepartment);
        log.trace("Success when updating {}", updatedDepartment);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes department by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed department by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Department not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Department not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "department's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing department with id={}", id);
        departmentService.delete(id);
        log.trace("Success when removing department with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
