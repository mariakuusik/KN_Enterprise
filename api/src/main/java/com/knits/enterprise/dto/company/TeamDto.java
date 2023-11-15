package com.knits.enterprise.dto.company;

import com.knits.enterprise.model.security.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class TeamDto extends AbstractOrganizationStructureDto{
}
