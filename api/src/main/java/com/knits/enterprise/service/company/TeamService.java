package com.knits.enterprise.service.company;

import com.knits.enterprise.dto.search.TeamResponseDto;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.dto.company.TeamCreateDto;
import com.knits.enterprise.dto.company.TeamEditDto;
import com.knits.enterprise.exceptions.TeamException;
import com.knits.enterprise.mapper.company.TeamMapper;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.model.security.User;
import com.knits.enterprise.repository.company.TeamRepository;
import com.knits.enterprise.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    TeamMapper teamMapper;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createNewTeam(TeamCreateDto teamCreateDto) {
        Boolean teamNameExists = teamRepository.existsByName(teamCreateDto.getTeamName());
        if (teamNameExists.equals(false)) {
            Team team = teamMapper.toCreateTeam(teamCreateDto);
            team.setStartDate(LocalDateTime.now());
            team.setEndDate(null);
            team.setActive(true);
            Optional<User> optionalUser = userRepository.findOneByLogin(teamCreateDto.getLogin());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                team.setCreatedBy(user);
                teamRepository.save(team);
            }
        } else throw new TeamException("Team with name " + teamCreateDto.getTeamName() + " already exists");
    }

    public void updateTeam(TeamEditDto teamEditDto, Long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            teamMapper.partialUpdate(teamEditDto, team);
            teamRepository.save(team);
        }
    }

    public void deactivateTeam(Long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            team.setEndDate(LocalDateTime.now());
            team.setActive(false);
        }
    }

    public List<TeamResponseDto> filterTeams(TeamSearchDto teamSearchDto) {
        Team teamSearchEntity = teamMapper.toSearchEntity(teamSearchDto);
        Optional<User> optionalUser = userRepository.findOneByLogin(teamSearchDto.getCreatedByLogin());
        if (optionalUser.isPresent()){
            teamSearchEntity.setCreatedBy(optionalUser.get());
        }
        List<Team> teams = teamRepository.findAllIncludingActive();
        List<Team> filteredTeams = teams
                .stream()
                .filter(team -> {
                    boolean include = true;
                    if (teamSearchDto.getTeamName() != null && !team.getName().equals(teamSearchDto.getTeamName())) {
                        include = false;
                    }
                    if (teamSearchDto.getStartDate() != null && !team.getStartDate().isBefore(teamSearchDto.getStartDate())){
                        include = false;
                    }
                    if (teamSearchDto.getEndDate() != null && !team.getStartDate().isAfter(teamSearchDto.getEndDate())){
                        include = false;
                    }

                    if (teamSearchDto.getCreatedByLogin() != null && !team.getCreatedBy().getLogin().equals(teamSearchDto.getCreatedByLogin())){
                        include = false;
                    }
                    return include;
                })
                .collect(Collectors.toList());
        return teamMapper.toTeamResponseDtos(filteredTeams);
    }
}
