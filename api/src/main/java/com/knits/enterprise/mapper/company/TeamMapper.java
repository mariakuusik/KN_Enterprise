package com.knits.enterprise.mapper.company;

import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.mapper.security.UserMapper;
import com.knits.enterprise.model.company.Team;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class})
public interface TeamMapper extends AbstractOrganizationStructureMapper<Team, TeamDto> {
    List<TeamDto> toDtos(List<Team> entityList);

}
