package it.com.knits.enterprise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.knits.enterprise.dto.TeamDtoMocks;
import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.TeamDto;
import it.com.knits.enterprise.security.UserTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
public class TeamIT {

    @Resource
    TeamTemplate teamTemplate;

    @Resource
    UserTemplate userTemplate;

    private static final String MOCK_NAME = "MockTestNameItTest";
    private static final String MOCK_UPDATED_NAME = "MockTeamNameItTest-Update";
    private static final String[] NAMES = {"Name 1", "Name 2", "Name3"};

    @Test
    public void testCreateNewTeamSuccess() throws Exception {
        String token = userTemplate.loginAndGetToken();
        TeamDto saved = createFlow(token, MOCK_NAME);

        TeamDto foundById = teamTemplate.findById(token, saved.getId());
        assertThat(foundById).usingRecursiveComparison().isEqualTo(saved);

        teamTemplate.delete(token, foundById.getId());
    }

    private TeamDto createFlow(String token, String name) throws Exception{
        TeamDto expected = TeamDtoMocks.testTeamDto(1L);
        expected.setName(name);
        return teamTemplate.create(token, expected);
    }

}
