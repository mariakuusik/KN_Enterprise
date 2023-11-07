package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.company.TeamCreateDto;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.dto.company.TeamEditDto;
import com.knits.enterprise.exceptions.TeamException;
import com.knits.enterprise.service.company.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api")
@Slf4j
public class TeamController {

    @Autowired
    private TeamService teamService;

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


}
