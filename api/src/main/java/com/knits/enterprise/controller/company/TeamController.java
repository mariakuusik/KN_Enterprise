package com.knits.enterprise.controller.company;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.service.company.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public ResponseEntity<TeamDto> createNewTeam(@RequestBody TeamDto teamDto) {
        return ResponseEntity
                .ok()
                .body(teamService.createNewTeam(teamDto));
    }

    @PutMapping(value = "/teams")
    @Operation(summary = "Edits existing team",
            description = "Finds team by teamId. Possible to edit: name, description, active, enddate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<TeamDto> updateTeam(@RequestBody TeamDto teamDto) {
        return ResponseEntity
                .ok()
                .body(teamService.updateTeam(teamDto));
    }

    @PatchMapping(value = "/teams/deactivate")
    @Operation(summary = "Deactivates active team",
            description = "Sets active to false and adds end-date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<Void> deactivateTeam(@RequestParam Long id) {
        teamService.deactivateTeam(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/teams")
    @Operation(summary = "Searches for teams by filters",
            description = "filters are startDate, endDate, createdBy, teamName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Denied")})
    public ResponseEntity<List<TeamDto>> findTeams(TeamSearchDto searchDto) {
        List<TeamDto> teamDtos = teamService.filterTeams(searchDto).getData();
        return ResponseEntity
                    .ok()
                    .body(teamDtos);
    }
}
