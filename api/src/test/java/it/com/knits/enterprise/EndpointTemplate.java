package it.com.knits.enterprise;

import com.knits.enterprise.dto.security.UserDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import it.com.knits.enterprise.config.EnvVars;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Component
public abstract class EndpointTemplate {
    @Autowired
    private Environment environment;

    protected String getBaseUrl() {
        return environment.getProperty(EnvVars.BASE_URL);
    }

    protected abstract String getEndpoint();

    protected UserDto getCurrentUser() {
        return UserDto.builder()
                .login(environment.getProperty(EnvVars.TEST_USERNAME))
                .password(environment.getProperty(EnvVars.TEST_PASSWORD))
                .build();
    }

    public void delete(String token, Long id) {
        Response response = httpDelete(token, id);
    }

    protected String getEndpointUrl() {
        System.out.println(getBaseUrl() + "/" + getEndpoint());
        return getBaseUrl() + "/" + getEndpoint();
    }

    public <T> Response httpPost(String token, T data, int expectedHttpCode) {
        RequestSpecification request = setupRequest(token, data);
        Response response = request.post(getEndpointUrl());
        assertThat(response.getStatusCode(), equalTo(expectedHttpCode));
        return response;
    }

    protected <T> Response httpPut(String token, T data, int expectedHttpCode) {
        RequestSpecification request = setupRequest(token, data);
        Response response = request.put(getEndpointUrl());
        assertThat(response.getStatusCode(), equalTo(expectedHttpCode));
        return response;
    }

    protected <T> Response httpPatch(String token, T data, int expectedHttpCode) {
        RequestSpecification request = setupRequest(token, data);
        Response response = request.patch(getEndpointUrl());
        assertThat(response.getStatusCode(), equalTo(expectedHttpCode));
        return response;
    }

    protected <T> Response httpDelete(String token, Long id) {
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer" + token);
        return request.delete(getEndpointUrl() + "/" + id);
    }

    protected <T> Response httpGetPathParams(String token, String pathParams, int expectedHttpCode) {
        RequestSpecification request = setupRequest(token);
        Response response = StringUtils.isNotEmpty(pathParams) ?
                request.get(getEndpointUrl() + "/" + pathParams) :
                request.get(getEndpointUrl());

        assertThat(response.getStatusCode(), equalTo(200));
        return response;
    }

    protected <T> Response httpGetQueryString(String token, String queryString, int expectedHttpCode) {
        RequestSpecification request = setupRequest(token);
        Response response = StringUtils.isNotEmpty(queryString) ?
                request.get(getEndpointUrl() + "?" + queryString) :
                request.get(getEndpointUrl());

        assertThat(response.getStatusCode(), equalTo(200));
        return response;
    }

    //private methods:
    private <T> RequestSpecification setupRequest(String token, T data) {
        RequestSpecification request = setupRequest(token);
        request.body(data);
        return request;
    }

    private RequestSpecification setupRequest(String token) {
        return StringUtils.isEmpty(token) ? unAuthorizedRequest() : authorizedRequest(token);
    }

    private RequestSpecification authorizedRequest(String token) {
        RequestSpecification request = unAuthorizedRequest();
        request.header("Authorization", "Bearer" + token);
        return request;
    }

    private RequestSpecification unAuthorizedRequest() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        return request;
    }

}

