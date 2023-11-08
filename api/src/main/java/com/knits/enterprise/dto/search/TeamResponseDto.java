package com.knits.enterprise.dto.search;

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
public class TeamResponseDto implements Serializable {
    private String teamName;
    private String teamDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String createdByLogin;
}