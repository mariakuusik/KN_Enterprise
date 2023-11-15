package com.knits.enterprise.dto.company;

import com.knits.enterprise.dto.security.UserDto;
import com.knits.enterprise.model.security.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class TeamDto extends AbstractOrganizationStructureDto{
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private UserDto createdBy;
}
