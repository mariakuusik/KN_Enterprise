package com.knits.enterprise.mapper.company;

import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.mapper.security.UserMapper;
import com.knits.enterprise.dto.company.TeamEditDto;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.dto.company.TeamCreateDto;
import org.mapstruct.*;

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
}
