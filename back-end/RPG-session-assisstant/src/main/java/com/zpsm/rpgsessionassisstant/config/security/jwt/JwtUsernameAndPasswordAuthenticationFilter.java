package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.util.ErrorsMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final ObjectMapper objectMapper;

    public JwtUsernameAndPasswordAuthenticationFilter(
        JwtService jwtService,
        AuthenticationManager authenticationManager,
        JwtConfig jwtConfig) {

        super(authenticationManager);
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        LoginRequest loginRequest = mapToLoginRequest(request);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            loginRequest.login(),
            loginRequest.password());

        return super.getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult) throws IOException, ServletException {

        Player player = (Player) authResult.getPrincipal();
        String accessToken = jwtService.accessToken(player);
        String refreshToken = jwtService.refreshToken(player);
        response.addHeader(jwtConfig.getAuthorizationHeader(), accessToken);
        response.addHeader("Refresh-Token", refreshToken);
        log.debug("Successful authentication");
    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {

        response.getOutputStream()
            .println(objectMapper.writeValueAsString(ErrorsMapper.getErrorsMap(failed.getMessage())));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private LoginRequest mapToLoginRequest(HttpServletRequest request) {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            if (!isLoginRequestCorrect(loginRequest)) {
                log.error("Incorrect login payload");
                throw new LoginException("Incorrect login payload");
            }
            return loginRequest;
        } catch (IOException e) {
            log.error("Couldn't map to LoginRequest", e);
            throw new LoginException("Couldn't map to LoginRequest", e);
        }
    }

    private boolean isLoginRequestCorrect(LoginRequest loginRequest) {
        return null != loginRequest.login() && !loginRequest.login().isBlank()
            && null != loginRequest.password() && !loginRequest.password().isBlank();
    }

}
