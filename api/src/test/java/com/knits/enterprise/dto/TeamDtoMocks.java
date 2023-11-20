package com.knits.enterprise.dto;

import com.knits.enterprise.config.Constants;
import com.knits.enterprise.dto.company.TeamDto;
import com.knits.enterprise.dto.security.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class TeamDtoMocks {

    public static TeamDto testTeamDto(Long id) {

        return TeamDto.builder()
                .id(id)
                .name("Test Name")
                .description("Test Description")
                .startDate(LocalDateTime.now().format(Constants.TIME_FORMATTER))
                .endDate(null)
                .createdBy(UserDtoMocks.createTestUserDto(1L))
                .build();
    }


}