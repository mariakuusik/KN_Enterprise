package it.com.knits.enterprise;

import com.knits.enterprise.dto.company.TeamDto;
import io.restassured.response.Response;
import it.com.knits.enterprise.util.ItTestConst;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class TeamTemplate extends EndpointTemplate {

    public TeamDto create(String token, TeamDto expected) {
        Response response = httpPost(token, expected, ItTestConst.HTTP_SUCCESS);
        TeamDto actual = response.getBody().as(TeamDto.class);
        assertThat(actual).usingRecursiveComparison().ignoringFields("id", "startDate", "endDate").isEqualTo(expected);
        return actual;
    }

    public TeamDto findById(String token, Long id) {
        Response response = httpGetPathParams(token, String.valueOf(id), ItTestConst.HTTP_SUCCESS);
        return response.getBody().as(TeamDto.class);
    }

    public TeamDto partialUpdate(String token, TeamDto expected) {
        Response response = httpPatch(token, expected, ItTestConst.HTTP_SUCCESS);
        TeamDto actual = response.getBody().as(TeamDto.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        return actual;
    }

    @Override
    protected String getEndpoint() {
        return "teams";
    }
}
