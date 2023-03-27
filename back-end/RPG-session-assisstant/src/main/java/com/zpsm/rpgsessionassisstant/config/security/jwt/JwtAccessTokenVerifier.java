package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (null == token || token.isBlank()) {
            log.debug("No token provided");
            filterChain.doFilter(request, response);
            return;
        }
        JWTVerifier verifier = buildJWTVerifier();
        DecodedJWT decodedJWT = verifier.verify(token);
        String login = decodedJWT.getSubject();
        Set<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("authorities").asList(String.class)
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
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

}
