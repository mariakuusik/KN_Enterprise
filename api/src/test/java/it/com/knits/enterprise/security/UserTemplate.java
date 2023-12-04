package it.com.knits.enterprise.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserTemplate {

    @Resource
    private final Environment environment;

    public UserTemplate(Environment environment) {
        this.environment = environment;
    }

    public String loginAndGetToken() {
        return "mockedToken";
    }

}
