package com.knits.enterprise.dto.company;

import com.knits.enterprise.dto.security.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class AbstractOrganizationStructureDto extends AbstractActiveDto {
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private UserDto createdBy;
}
