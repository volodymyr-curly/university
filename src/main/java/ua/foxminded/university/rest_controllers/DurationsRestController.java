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
import ua.foxminded.university.domain.Duration;
import ua.foxminded.university.domain.ErrorResponse;
import ua.foxminded.university.dto.duration.DurationRequestDTO;
import ua.foxminded.university.dto.duration.DurationResponseDTO;
import ua.foxminded.university.service.interfaces.DurationService;
import ua.foxminded.university.util.mappers.DurationMapper;
import ua.foxminded.university.util.validators.DurationValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Duration", description = "The Duration API")
@RestController
@RequestMapping("/api/durations")
public class DurationsRestController extends DefaultController {

    private final DurationService durationService;
    private final DurationMapper durationMapper;
    private final DurationValidator durationValidator;

    @Autowired
    public DurationsRestController(DurationService durationService,
                                   DurationMapper durationMapper,
                                   DurationValidator durationValidator) {
        this.durationService = durationService;
        this.durationMapper = durationMapper;
        this.durationValidator = durationValidator;
    }

    @Operation(summary = "Gets all durations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all durations",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = DurationRequestDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Durations not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<DurationRequestDTO> showAll() {
        log.debug("Searching to show all durations");
        List<DurationRequestDTO> durationsDTO = durationMapper.getDurationsDTO();
        log.trace("Show all {}", durationsDTO);
        return durationMapper.getDurationsDTO();
    }

    @Operation(summary = "Gets duration by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found duration by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = DurationResponseDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Duration not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Duration not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}")
    public DurationResponseDTO show(@Parameter(description = "duration's id to be searched")
                                    @PathVariable("id") int id) {
        log.debug("Searching duration with id={}", id);
        DurationResponseDTO durationDetailedDTO = durationMapper
            .convertToDurationResponseDTO(durationService.find(id));
        log.trace("Show {}", durationDetailedDTO);
        return durationDetailedDTO;
    }

    @Operation(summary = "Creates new duration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new duration",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Duration had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid DurationRequestDTO durationDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", durationDTO);
        durationValidator.validate(durationDTO, bindingResult);
        checkForErrors(bindingResult);
        Duration duration = durationMapper.convertToDuration(durationDTO);
        durationService.add(duration);
        log.trace("Success when save {}", duration);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets duration by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found duration by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = DurationRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Duration not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Duration not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public DurationRequestDTO edit(@Parameter(description = "duration's id to be searched to update")
                                   @PathVariable("id") int id) {
        log.debug("Searching for updating duration with id={}", id);
        DurationRequestDTO durationDTO = durationMapper.convertToDurationRequestDTO(durationService.find(id));
        log.trace("Success when searching {}", durationDTO);
        return durationDTO;
    }

    @Operation(summary = "Updates duration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated duration",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Duration had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Duration not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Duration not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid DurationRequestDTO durationDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "duration's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating duration with id={} to {}", id, durationDTO);
        durationDTO.setId(id);
        durationValidator.validate(durationDTO, bindingResult);
        checkForErrors(bindingResult);
        Duration updatedDuration = durationMapper.convertToDuration(durationDTO);
        durationService.update(id, updatedDuration);
        log.trace("Success when save {}", updatedDuration);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes duration by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed duration by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Duration not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Duration not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "duration's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing duration with id={}", id);
        durationService.delete(id);
        log.trace("Success when removing duration with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
