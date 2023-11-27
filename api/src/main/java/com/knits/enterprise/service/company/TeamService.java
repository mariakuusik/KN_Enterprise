package com.knits.enterprise.service.company;

import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.OnCreate;
import com.knits.enterprise.dto.company.OnUpdate;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.TeamMapper;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.repository.company.TeamRepository;
import com.knits.enterprise.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class TeamService {
    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;
    private final UserService userService;

    @Transactional
    @Validated(OnCreate.class)
    public TeamDto createNewTeam(@Valid TeamDto teamDto) {
        if (teamRepository.existsByName(teamDto.getName())) {
            throw new UserException("Team with name " + teamDto.getName() + " already exists");
        }
        Team team = teamMapper.toEntity(teamDto);
        team.setStartDate(LocalDateTime.now());
        team.setCreatedBy(userService.getCurrentUser());
        Team newTeam = teamRepository.save(team);
        return teamMapper.toDto(newTeam);
    }

    @Validated(OnUpdate.class)
    public TeamDto updateTeam(@Valid TeamDto teamDto) {
        Team team = teamRepository.findById(teamDto.getId())
                .orElseThrow(() -> new UserException("Team with id " + teamDto.getId() + " was not found"));
        teamMapper.partialUpdate(team, teamDto);
        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toDto(updatedTeam);
    }

    public void deactivateTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new UserException("Active team with id " + id + " was not found"));
        team.setActive(false);
    }

    public PaginatedResponseDto<List<TeamDto>> filterTeams(TeamSearchDto teamSearchDto) {

        Page<Team> teamPage = teamRepository.findAll(teamSearchDto.getSpecification(), teamSearchDto.getPageable());
        List<TeamDto> teamDtos = teamMapper.toDtos(teamPage.getContent());

        return PaginatedResponseDto.<List<TeamDto>>builder()
                .page(teamSearchDto.getPage())
                .size(teamDtos.size())
                .sortingFields(teamSearchDto.getSort())
                .sortDirection(teamSearchDto.getDir().name())
                .data(Collections.singletonList(teamDtos))
                .build();
    }
}
