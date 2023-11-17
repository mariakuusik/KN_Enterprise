package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.error.ApiError;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.service.company.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TeamController {
    private final TeamService teamService;

    @PostMapping(value = "/teams")
    @Operation(summary = "Creates new Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Could not create new team")})
    public ResponseEntity<TeamDto> createNewTeam(@Valid @RequestBody TeamDto teamDto) {
        return ResponseEntity
                .ok()
                .body(teamService.createNewTeam(teamDto));
    }

    @PutMapping(value = "/teams")
    @Operation(summary = "Edits existing team",
            description = "Finds team by teamId. Possible to edit: name, description, active, startdate enddate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Team with this ID was not found")})
    public ResponseEntity<?> updateTeam(@Valid @RequestBody TeamDto teamDto) {
        try {
            TeamDto updatedTeam = teamService.updateTeam(teamDto);
            return ResponseEntity.ok(updatedTeam);
        } catch (UserException e) {
            ApiError apiError = new ApiError();
            apiError.setErrorCode(HttpStatus.BAD_REQUEST.value());
            apiError.setMessage("Active Team with ID of " + teamDto.getId() + " was not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        }
    }

    @PatchMapping(value = "/teams/deactivate")
    @Operation(summary = "Deactivates active team",
            description = "Sets active to false. Doesn't add end-date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> deactivateTeam(
            @RequestParam
            @NotNull(message = "ID is mandatory")
            @Positive(message = "Value must be greater than 0")
            Long id) {
        try {
            teamService.deactivateTeam(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Active Team with ID of " + id + " was not found");
        }
    }

    @GetMapping(value = "/teams")
    @Operation(summary = "Searches for teams by filters",
            description = "filters are startDate, endDate, createdBy, teamName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Denied")})
    public ResponseEntity<PaginatedResponseDto<List<TeamDto>>> findTeams(TeamSearchDto searchDto) {
        PaginatedResponseDto<List<TeamDto>> paginatedResponse = teamService.filterTeams(searchDto);
        return ResponseEntity
                .ok()
                .body(paginatedResponse);
    }
}

