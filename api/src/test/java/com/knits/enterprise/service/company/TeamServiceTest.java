package com.knits.enterprise.service.company;

import com.knits.enterprise.dto.TeamDtoMocks;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.TeamMapper;
import com.knits.enterprise.mapper.company.TeamMapperImpl;
import com.knits.enterprise.mapper.security.UserMapper;
import com.knits.enterprise.mapper.security.UserMapperImpl;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.repository.company.TeamRepository;
import com.knits.enterprise.service.security.UserService;
import com.sun.tools.javac.Main;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Spy
    private TeamMapper teamMapper = new TeamMapperImpl();

    @Mock
    private TeamRepository teamRepository;

    @Mock
    UserService userService;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCreateNewTeam() {
        TeamDto toCreateNewTeam = TeamDtoMocks.testTeamDto(1L);
        when(teamRepository.save(Mockito.any(Team.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TeamDto newTeam = teamService.createNewTeam(toCreateNewTeam);

        verify(teamRepository, times(1)).save(Mockito.any());
        verify(teamMapper, times(1)).toEntity(toCreateNewTeam);
        verify(teamMapper, times(1)).toDto(Mockito.any(Team.class));
        verify(userService, times(1)).getCurrentUser();

        assertNotNull(newTeam);
        assertEquals(toCreateNewTeam.getName(), newTeam.getName());
        assertThat(toCreateNewTeam).isEqualTo(newTeam);
    }

    @Test
    void shouldFailWithDuplicatedTeamName() {
        TeamDto teamDto = TeamDtoMocks.testTeamDto(1L);
        TeamDto duplicate = TeamDtoMocks.testTeamDto(1L);
        teamService.createNewTeam(teamDto);
        UserException userException = assertThrows(UserException.class, () ->
        {
            teamService.createNewTeam(duplicate);
        });
        assertTrue(userException.getMessage().contains("Team with name " + duplicate.getName() + " already exists"));
    }

    @Test
    void shouldUpdateTeam() {
    }

    @Test
    void shouldDeactivateTeam() {
    }

    @Test
    void filterTeams() {
    }
}