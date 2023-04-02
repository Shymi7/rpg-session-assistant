package com.zpsm.rpgsessionassisstant.config.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.zpsm.rpgsessionassisstant.config.security.jwt.JwtAccessTokenVerifier;
import com.zpsm.rpgsessionassisstant.config.security.jwt.JwtConfig;
import com.zpsm.rpgsessionassisstant.config.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Clock;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig {

    private final Algorithm algorithm;
    private final JwtConfig jwtConfig;
    private final Clock clock;

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        AuthenticationManager authenticationManager) throws Exception {

        return http
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/registration").permitAll()
                    .requestMatchers("/api/token/refresh").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers(PathRequest.toH2Console()).permitAll()
                    .anyRequest().authenticated())
            .headers()
            .frameOptions()
            .sameOrigin()
            .and()
            .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
                authenticationManager,
                jwtConfig,
                algorithm,
                clock))
            .addFilterAfter(new JwtAccessTokenVerifier(jwtConfig, algorithm, clock), JwtUsernameAndPasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
        UserDetailsService playerDetailsService,
        PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(playerDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        HttpSecurity http,
        DaoAuthenticationProvider provider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(provider)
            .build();
    }

}
