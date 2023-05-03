package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zpsm.rpgsessionassisstant.util.ErrorsMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Clock;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class JwtAccessTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final Algorithm algorithm;
    private final Clock clock;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (jwtConfig.getIgnoredPaths().contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (null == token || token.isBlank()) {
            filterChain.doFilter(request, response);
            log.error("No token provided");
            return;
        }
        DecodedJWT decodedJWT = verifyToken(token, response);
        if (null == decodedJWT) {
            filterChain.doFilter(request, response);
            log.error("Token could not be verified");
            return;
        }
        String login = decodedJWT.getSubject();
        Set<SimpleGrantedAuthority> authorities = mapAuthorities(decodedJWT);
        Authentication authentication = new UsernamePasswordAuthenticationToken(login, null, authorities);
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
        filterChain.doFilter(request, response);
        log.debug("Successful JWT verification");
    }

    private JWTVerifier buildJWTVerifier() {
        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(algorithm);
        return verification.build(clock);
    }

    private Set<SimpleGrantedAuthority> mapAuthorities(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("authorities")
            .asList(String.class)
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
    }

    private DecodedJWT verifyToken(String token, HttpServletResponse outResponse) throws IOException {
        DecodedJWT decodedJWT = null;
        try {
            JWTVerifier verifier = buildJWTVerifier();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            log.error("Token could not be verified", e);
            outResponse.getOutputStream()
                .println(objectMapper.writeValueAsString(ErrorsMapper.getErrorsMap(e.getMessage())));
            outResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return decodedJWT;
    }

}
