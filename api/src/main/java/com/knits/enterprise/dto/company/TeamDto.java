package com.knits.enterprise.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.knits.enterprise.dto.security.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class TeamDto extends AbstractOrganizationStructureDto {
    @NotNull(groups = OnUpdate.class, message = "Cannot insert new ID")
    public Long getId() {
        return super.getId();
    }
    @NotBlank(message = "Validation error: Name cannot be empty")
    @NotNull(message = "Validation error: Name cannot be null")
    public String getName() {
        return super.getName();
    }
    private String description;
    private String startDate;
    private String endDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private UserDto createdBy;
}
