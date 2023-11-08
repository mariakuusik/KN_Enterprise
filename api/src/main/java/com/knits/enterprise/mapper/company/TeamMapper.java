package com.knits.enterprise.mapper.company;

import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.mapper.security.UserMapper;
import com.knits.enterprise.dto.company.TeamEditDto;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.dto.company.TeamCreateDto;
import com.knits.enterprise.dto.search.TeamResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class})
public interface TeamMapper extends AbstractOrganizationStructureMapper<Team, TeamDto> {

    @Mapping(source = "teamDescription", target = "description")
    @Mapping(source = "teamName", target = "name")
    Team toCreateTeam(TeamCreateDto teamCreateDto);

    @Mapping(source = "teamName", target = "name")
    @Mapping(source = "teamDescription", target = "description")
    @Mapping(source = "endDate", target = "endDate")
    Team partialUpdate(TeamEditDto teamEditDto, @MappingTarget Team team);

    @Mapping(source = "teamName", target = "name")
    Team toSearchEntity(TeamSearchDto teamSearchDto);

    @Mapping(source = "name", target = "teamName")
    @Mapping(source = "description", target = "teamDescription")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    TeamResponseDto toTeamResponseDto(Team team);
    List<TeamResponseDto> toTeamResponseDtos(List<Team> teams);


}
