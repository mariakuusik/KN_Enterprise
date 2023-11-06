package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.company.TeamCreateDto;
import com.knits.enterprise.service.company.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(responseCode = "403", description = "Could not create new team")
    })
    public void createNewTeam(@RequestBody TeamCreateDto teamCreateDto) {
        teamService.createNewTeam(teamCreateDto);
    }


}
