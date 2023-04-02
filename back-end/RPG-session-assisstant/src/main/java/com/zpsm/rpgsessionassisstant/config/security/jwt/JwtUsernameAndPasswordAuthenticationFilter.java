package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.util.Date;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final Algorithm algorithm;
    private final Clock clock;
    private final ObjectMapper objectMapper;

    public JwtUsernameAndPasswordAuthenticationFilter(
        AuthenticationManager authenticationManager,
        JwtConfig jwtConfig,
        Algorithm algorithm,
        Clock clock) {

        super(authenticationManager);
        this.jwtConfig = jwtConfig;
        this.algorithm = algorithm;
        this.clock = clock;
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
        String accessToken = accessToken(player, authResult);
        String refreshToken = refreshToken(player);
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

    private String accessToken(Player player, Authentication authResult) {
        return JWT.create()
            .withSubject(player.getLogin())
            .withIssuedAt(Date.from(clock.instant()))
            .withExpiresAt(accessTokenExpiration())
            .withClaim("authorities", authResult.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
            .sign(algorithm);
    }

    private String refreshToken(Player player) {
        return JWT.create()
            .withSubject(player.getLogin())
            .withIssuedAt(Date.from(clock.instant()))
            .withExpiresAt(refreshTokenExpiration())
            .sign(algorithm);
    }

    private Date accessTokenExpiration() {
        return Date.from(clock.instant().plus(Duration.ofHours(jwtConfig.getAccessTokenExpirationAfterHours())));
    }

    private Date refreshTokenExpiration() {
        return Date.from(clock.instant().plus(Duration.ofHours(jwtConfig.getRefreshTokenExpirationAfterHours())));
    }

    private boolean isLoginRequestCorrect(LoginRequest loginRequest) {
        return null != loginRequest.login() && !loginRequest.login().isBlank()
            && null != loginRequest.password() && !loginRequest.password().isBlank();
    }

}
