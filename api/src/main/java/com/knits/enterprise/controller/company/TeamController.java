package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.search.TeamResponseDto;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.dto.company.TeamCreateDto;
import com.knits.enterprise.dto.company.TeamEditDto;
import com.knits.enterprise.mapper.company.TeamMapper;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.service.company.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamMapper teamMapper;

    @PostMapping(value = "/teams")
    @Operation(summary = "Creates new Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Could not create new team")})
    public void createNewTeam(@RequestBody TeamCreateDto teamCreateDto) {
        teamService.createNewTeam(teamCreateDto);
    }

    @PutMapping(value = "/teams")
    @Operation(summary = "Edits excisting team",
            description = "Finds team by teamId. Possible to edit: name, description, active, enddate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public void updateTeam(@RequestBody TeamEditDto teamEditDto, @RequestParam Long id) {
        teamService.updateTeam(teamEditDto, id);
    }

    @PatchMapping(value = "/teams/deactivate")
    @Operation(summary = "Deactivates active team",
            description = "Sets active to false and adds end-date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public void deactivateTeam(@RequestParam Long id) {
        teamService.deactivateTeam(id);
    }

    @GetMapping(value = "/teams")
    @Operation(summary = "Search for teams by filters",
            description = "filters are startDate, endDate, createdBy, teamName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Denied"),
            @ApiResponse(responseCode = "404", description = "No teams were found based on these filters")
    })
    public ResponseEntity<List<TeamResponseDto>> findTeams(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "startDateFrom", required = false) LocalDateTime startDateFrom,
            @RequestParam(name = "startDateTo", required = false) LocalDateTime startDateTo,
            @RequestParam(name = "createdByUserName", required = false) String createdByUsername
    ) {
        TeamSearchDto teamSearchDto = new TeamSearchDto();
        teamSearchDto.setTeamName(name);
        teamSearchDto.setStartDate(startDateFrom);
        teamSearchDto.setEndDate(startDateTo);
        teamSearchDto.setCreatedByLogin(createdByUsername);
        List<TeamResponseDto> teams = teamService.filterTeams(teamSearchDto);

        if (teams.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(teams);
        }
    }
}
