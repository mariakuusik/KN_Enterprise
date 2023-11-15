package com.knits.enterprise.dto.company;

import com.knits.enterprise.model.company.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Team}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamEditDto implements Serializable {
    private boolean active;
    private String teamName;
    private String teamDescription;
    private LocalDateTime endDate;
}