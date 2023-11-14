package com.knits.enterprise.service.company;

import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.TeamMapper;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.model.security.User;
import com.knits.enterprise.repository.company.TeamRepository;
import com.knits.enterprise.repository.security.UserRepository;
import com.knits.enterprise.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

private final TeamMapper teamMapper;
private final TeamRepository teamRepository;
private final UserRepository userRepository;
private final UserService userService;

    @Transactional
    public TeamDto createNewTeam(TeamDto teamDto) {
        Boolean teamNameExists = teamRepository.existsByName(teamDto.getName());
        if (!teamNameExists) {
            Team team = teamMapper.toEntity(teamDto);
            team.setStartDate(LocalDateTime.now());
            User currentUser = userService.getCurrentUser();
            team.setCreatedBy(currentUser);
            Team newTeam = teamRepository.save(team);
            return teamMapper.toDto(newTeam);
        } else throw new UserException("Team with name " + teamDto.getName() + " already exists");
    }

    public TeamDto updateTeam(TeamDto teamDto) {
        Optional<Team> optionalTeam = teamRepository.findById(teamDto.getId());
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            teamMapper.partialUpdate(teamDto, team);
            Team updatedTeam = teamRepository.save(team);
            return teamMapper.toDto(updatedTeam);
        } else throw new UserException("Team with name " + teamDto.getName() + " was not found");
    }

    public void deactivateTeam(Long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        optionalTeam.ifPresent(team -> teamRepository.deleteById(team.getId()));
    }

    public PaginatedResponseDto<TeamDto> filterTeams(TeamSearchDto searchDto) {

        Page<Team> teamPage = teamRepository.findAll(searchDto.getSpecification(), searchDto.getPageable());
        List<TeamDto> teamDtos = teamMapper.toDtos(teamPage.getContent());

        return PaginatedResponseDto.<TeamDto>builder()
                .page(searchDto.getPage())
                .size(teamDtos.size())
                .sortingFields(searchDto.getSort())
                .sortDirection(searchDto.getSort())
                .data(teamDtos)
                .build();
    }
}
