package com.knits.enterprise.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.knits.enterprise.dto.security.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class GroupDto extends AbstractOrganizationStructureDto{
    private Long id;
    private String name;
    private String description;
    private String startDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String endDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private UserDto createdBy;
}
