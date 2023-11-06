package com.knits.enterprise.service.company;

import com.knits.enterprise.dto.company.TeamCreateDto;
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
import java.util.Optional;

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
        if(teamNameExists.equals(false)){
            Team team = teamMapper.toCreateTeam(teamCreateDto);
            team.setStartDate(LocalDateTime.now());
            team.setEndDate(null);
            team.setActive(true);
            Optional<User> optionalUser = userRepository.findOneByLogin(teamCreateDto.getLogin());
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                team.setCreatedBy(user);
                teamRepository.save(team);
            }
        } else throw new TeamException("Team with name " + teamCreateDto.getTeamName() + " already exists");

    }
}
