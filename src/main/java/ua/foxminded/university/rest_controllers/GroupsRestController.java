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
import ua.foxminded.university.domain.Group;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.dto.group.GroupResponseDTO;
import ua.foxminded.university.service.interfaces.GroupService;
import ua.foxminded.university.util.mappers.GroupMapper;
import ua.foxminded.university.util.validators.GroupValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Group", description = "The Group API")
@RestController
@RequestMapping("/api/groups")
public class GroupsRestController extends DefaultController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final GroupValidator groupValidator;

    @Autowired
    public GroupsRestController(GroupService groupService,
                                GroupMapper groupMapper,
                                GroupValidator groupValidator) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
        this.groupValidator = groupValidator;
    }

    @Operation(summary = "Gets all groups")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all groups",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = GroupResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Groups not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping()
    public List<GroupResponseDTO> showAll() {
        log.debug("Searching to show all groups");
        List<GroupResponseDTO> groupsDTO = groupService.findAll().stream()
            .map(groupMapper::convertToGroupResponseDTOWhenFindAll)
            .toList();
        log.trace("Show all {}", groupsDTO);
        return groupsDTO;
    }

    @Operation(summary = "Gets groups list by department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found groups list by department",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = GroupResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Groups not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/department/list")
    public List<GroupResponseDTO> showByDepartment(@Parameter(description = "departments's id to be searched")
                                                   @RequestParam("id") int id) {
        log.debug("Searching by newDepartment id={}", id);
        List<GroupResponseDTO> groupsDTO = groupService.findByDepartment(id).stream()
            .map(groupMapper::convertToGroupResponseDTOWhenFindAll)
            .toList();
        log.trace("Show by newDepartment id {}", groupsDTO);
        return groupsDTO;
    }

    @Operation(summary = "Gets groups list by subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found groups list by subject",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = GroupResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Groups not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/subject/list")
    public List<GroupResponseDTO> showBySubject(@Parameter(description = "subject's id to be searched")
                                                @RequestParam("id") int id) {
        log.debug("Searching by subject id={}", id);
        List<GroupResponseDTO> groupsDTO = groupService.findBySubject(id).stream()
            .map(groupMapper::convertToGroupResponseDTOWhenFindAll)
            .toList();
        log.trace("Show by subject id {}", groupsDTO);
        return groupsDTO;
    }

    @Operation(summary = "Gets groups list by lecture")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found groups list by lecture",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = GroupResponseDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "Groups not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/lecture/list")
    public List<GroupResponseDTO> showByLecture(@Parameter(description = "lecture's id to be searched")
                                                @RequestParam("id") int id) {
        log.debug("Searching by newLecture id={}", id);
        List<GroupResponseDTO> groupsDTO = groupService.findByLecture(id).stream()
            .map(groupMapper::convertToGroupResponseDTOWhenFindAll)
            .toList();
        log.trace("Show by newLecture id {}", groupsDTO);
        return groupsDTO;
    }

    @Operation(summary = "Gets group by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found group by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = GroupResponseDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Group not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Group not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}")
    public GroupResponseDTO show(@Parameter(description = "group's id to be searched")
                                 @PathVariable("id") int id) {
        log.debug("Searching group with id={}", id);
        GroupResponseDTO groupDTO = groupMapper.convertToGroupResponseDTO(groupService.find(id));
        log.trace("Show {}", groupDTO);
        return groupDTO;
    }

    @Operation(summary = "Creates new group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created new group",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Group had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid GroupRequestDTO groupDTO,
                                             BindingResult bindingResult) {
        log.debug("Saving {}", groupDTO);
        groupValidator.validate(groupDTO, bindingResult);
        checkForErrors(bindingResult);
        Group group = groupMapper.convertToGroup(groupDTO);
        groupService.add(group);
        log.trace("Success when saving {}", group);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Gets group by its id to update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found group by its id to update",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = GroupRequestDTO.class))}),
        @ApiResponse(responseCode = "404", description = "Group not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Group not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @GetMapping("/{id}/edit")
    public GroupRequestDTO edit(@Parameter(description = "group's id to be searched to update")
                                @PathVariable("id") int id) {
        log.debug("Searching for updating group with id={}", id);
        GroupRequestDTO groupDTO = groupMapper.convertToGroupRequestDTO(groupService.find(id));
        log.trace("Success when searching {}", groupDTO);
        return groupDTO;
    }

    @Operation(summary = "Updates group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated group",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "400", description = "Group had validation errors or already existed," +
            " threw Validation Exception",
            content = {@Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
        @ApiResponse(responseCode = "404", description = "Group not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Group not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid GroupRequestDTO groupDTO,
                                             BindingResult bindingResult,
                                             @Parameter(description = "group's id to be updated")
                                             @PathVariable("id") int id) {
        log.debug("Updating group with id={} to {}", id, groupDTO);
        groupDTO.setId(id);
        groupValidator.validate(groupDTO, bindingResult);
        checkForErrors(bindingResult);
        Group updatedGroup = groupMapper.convertToGroup(groupDTO);
        groupService.update(id, updatedGroup);
        log.trace("Success when updating {}", updatedGroup);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Deletes group by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Removed group by its id",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(example = "OK"))}),
        @ApiResponse(responseCode = "404", description = "Group not found",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(example = "Group not found"))),
        @ApiResponse(responseCode = "500", description = SERVER_ERROR_DESCRIPTION,
            content = @Content(mediaType = "text/plain"))})
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "group's id to be removed")
                                             @PathVariable("id") int id) {
        log.debug("Removing group with id={}", id);
        groupService.delete(id);
        log.trace("Success when removing group with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
