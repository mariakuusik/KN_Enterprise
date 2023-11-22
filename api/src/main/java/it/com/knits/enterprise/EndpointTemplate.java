package it.com.knits.enterprise;

import it.com.knits.enterprise.config.EnvVars;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

public abstract class EndpointTemplate {
    @Resource
    private Environment environment;

    protected String getBaseUrl() {
        return (String) environment.getProperty(EnvVars.BASE_URL);
    }

    protected abstract String getEndpoint();
}
