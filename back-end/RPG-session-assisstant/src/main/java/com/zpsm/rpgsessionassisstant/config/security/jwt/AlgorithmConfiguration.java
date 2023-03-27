package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AlgorithmConfiguration {

    private final JwtConfig jwtConfig;

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(jwtConfig.getSecret());
    }

}
