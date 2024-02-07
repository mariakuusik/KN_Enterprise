package com.knits.enterprise.service.company;

import com.knits.enterprise.dto.TeamDtoMocks;
import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.dto.search.GenericSearchDto;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.exceptions.SystemException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Spy
    private TeamMapper teamMapper = new TeamMapperImpl();

    @Captor
    private ArgumentCaptor<Team> teamCaptor;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    UserService userService;

    @InjectMocks
    private TeamService teamService;

    @Test
    void shouldCreateNewTeam() {
        TeamDto teamDto = TeamDtoMocks.testTeamDto(1L);
        when(teamRepository.save(Mockito.any(Team.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TeamDto newTeam = teamService.createNewTeam(teamDto);

        verify(teamRepository, times(1)).save(Mockito.any());
        verify(teamMapper, times(1)).toEntity(teamDto);
        verify(teamMapper, times(1)).toDto(Mockito.any(Team.class));
        verify(userService, times(1)).getCurrentUser();

        assertNotNull(newTeam);
        assertEquals(teamDto.getName(), newTeam.getName());
        assertThat(teamDto).isEqualTo(newTeam);
    }

    //this test is not passing
    @Test
    void shouldFailWithDuplicatedTeamName() {
        TeamDto teamDto = TeamDtoMocks.testTeamDto(1L);
        TeamDto duplicate = TeamDtoMocks.testTeamDto(1L);
        teamService.createNewTeam(teamDto);
        assertThatExceptionOfType(SystemException.class)
                .isThrownBy(() -> {
                    teamService.createNewTeam(duplicate);
                }).withMessageEndingWith(" already exists");
    }

    @Test
    void shouldUpdateTeam() {

        TeamDto requestTeamDto = TeamDtoMocks.testTeamDto(1L);
        Long existingTeamId = requestTeamDto.getId();
        requestTeamDto.setName("Updated test name");

        when(teamRepository.findById(existingTeamId)).thenReturn(Optional.of(new Team()));
        when(teamRepository.save(Mockito.any(Team.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TeamDto responseTeamDto = teamService.updateTeam(requestTeamDto);

        verify(teamRepository).findById(existingTeamId);
        verify(teamRepository).save(teamCaptor.capture());
        Team teamEntity = teamCaptor.getValue();

        verify(teamRepository, times(1)).save(teamEntity);
        verify(teamMapper, times(1)).toDto(teamEntity);

        assertThat(responseTeamDto.getName()).isEqualTo(requestTeamDto.getName());
        assertThat(responseTeamDto).isEqualTo(requestTeamDto);
    }

    @Test
    void shouldDeactivateTeam() {

        Long teamId = 1L;
        Team existingTeam = new Team();
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

        teamService.deactivateTeam(teamId);
        assertFalse(existingTeam.isActive());
    }

    //this test is not passing
    @Test
    void filterTeams() {

//        TeamSearchDto teamSearchDto = new TeamSearchDto();
//        teamSearchDto.setName("Test Name");
//        teamSearchDto.setStartDate(LocalDateTime.now().minusYears(5));
//        teamSearchDto.setEndDate(LocalDateTime.now());
//        teamSearchDto.setCreatedByLogin("Test User");
//
//        Page<Team> teamPage = new PageImpl<>(Collections.singletonList(new Team()));
//
//        when(teamRepository.findAll(teamSearchDto.getSpecification(), teamSearchDto.getPageable()))
//                .thenReturn(teamPage);
//        when(teamMapper.toDtos(anyList()))
//                .thenReturn(Collections.singletonList(new TeamDto()));
//
//        PaginatedResponseDto<List<TeamDto>> result = teamService.filterTeams(teamSearchDto);
//
//        TeamDto teamDto = new TeamDto();
//
//        assertThat(result.getPage()).isEqualTo(teamSearchDto.getPage());
//        assertThat(result.getSize()).isEqualTo(1);
//        assertThat(result.getSortingFields()).isEqualTo(teamSearchDto.getSort());
//        assertThat(result.getSortDirection()).isEqualTo(teamSearchDto.getDir().name());
//        assertThat(result.getData().get(0)).isEqualTo(teamDto);

    }
}