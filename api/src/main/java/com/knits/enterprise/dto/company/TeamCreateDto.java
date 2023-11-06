package com.knits.enterprise.dto.company;

import com.knits.enterprise.model.company.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for {@link Team}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateDto implements Serializable {
    @NotNull
    private String teamName;
    private String teamDescription;
    private String login;
}