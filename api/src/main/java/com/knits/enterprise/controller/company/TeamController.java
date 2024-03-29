package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.error.ApiError;
import com.knits.enterprise.service.company.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
            @ApiResponse(responseCode = "400", description = "RequestBody validation error"),
            @ApiResponse(responseCode = "404", description = "Team with this ID already exists")})
    public ResponseEntity<TeamDto> createNewTeam(@Valid @RequestBody TeamDto teamDto) {
        return ResponseEntity
                .ok()
                .body(teamService.createNewTeam(teamDto));
    }

    @PutMapping(value = "/teams")
    @Operation(summary = "Edits existing Team",
            description = "Finds Team by teamId. Possible to edit: name, description, active, startDate endDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Team with this ID was not found")})
    public ResponseEntity<TeamDto> updateTeam(@Valid @RequestBody TeamDto teamDto) {
        return ResponseEntity
                .ok()
                .body(teamService.updateTeam((teamDto)));
    }

    @PatchMapping(value = "/teams/deactivate")
    @Operation(summary = "Deactivates active Team",
            description = "Sets 'active' to false in DB. Doesn't add endDate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Team with this ID was not found"),
            @ApiResponse(responseCode = "422", description = "RequestParam validation error. Value of ID must be greater that 0")})
    public ResponseEntity<Null> deactivateTeam(
            @RequestParam
            @Min(value = 1, message = "Value of ID must be greater than 0")
            @Validated
            Long id) {
        teamService.deactivateTeam(id);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping(value = "/teams")
    @Operation(summary = "Searches for Teams by filters",
            description = "Filters are startDate, endDate, createdBy, teamName. Response can be paginated and sorted alphabetically.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Denied")})
    public ResponseEntity<PaginatedResponseDto<List<TeamDto>>> findTeams(TeamSearchDto teamSearchDto) {
        PaginatedResponseDto<List<TeamDto>> paginatedResponseFilteredTeams = teamService.filterTeams(teamSearchDto);
        return ResponseEntity
                .ok()
                .body(paginatedResponseFilteredTeams);
    }
}

