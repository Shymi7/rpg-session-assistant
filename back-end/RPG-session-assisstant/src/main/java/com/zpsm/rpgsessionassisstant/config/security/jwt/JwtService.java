package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zpsm.rpgsessionassisstant.exception.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    private final Algorithm algorithm;
    private final Clock clock;
    private final PlayerRepository playerRepository;

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (null == refreshToken || refreshToken.isBlank()) {
            throw new MissingTokenException("Refresh token is missing");
        }
        JWTVerifier jwtVerifier = buildJWTVerifier();
        DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
        String playerName =  decodedJWT.getSubject();
        Player player = playerRepository.findByLogin(playerName)
            .orElseThrow(() -> {
                log.error("Player with login {} not found", playerName);
                return new PlayerNotFoundException(String.format("Player with login %s not found", playerName));
            });
        String accessToken = accessToken(player);
        response.addHeader(jwtConfig.getAuthorizationHeader(), accessToken);
        log.debug("Token refreshed successfully");
    }

    public String accessToken(Player player) {
        return JWT.create()
            .withSubject(player.getLogin())
            .withIssuedAt(Date.from(clock.instant()))
            .withExpiresAt(accessTokenExpiration())
            .withClaim("authorities", player.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
            .sign(algorithm);
    }

    public String refreshToken(Player player) {
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

    private JWTVerifier buildJWTVerifier() {
        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(algorithm);
        return verification.build(clock);
    }

}
