package com.zpsm.rpgsessionassisstant.config.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;
    private Integer accessTokenExpirationAfterHours;
    private Integer refreshTokenExpirationAfterHours;
    private List<String> ignoredPaths;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

}
