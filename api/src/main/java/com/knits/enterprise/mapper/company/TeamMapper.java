package com.knits.enterprise.mapper.company;

import com.knits.enterprise.config.Constants;
import com.knits.enterprise.dto.search.TeamSearchDto;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.mapper.security.UserMapper;
import com.knits.enterprise.dto.company.TeamEditDto;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.dto.search.TeamResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class})
public interface TeamMapper extends AbstractOrganizationStructureMapper<Team, TeamDto> {
    List<TeamDto> toDtos(List<Team> entityList);
}
