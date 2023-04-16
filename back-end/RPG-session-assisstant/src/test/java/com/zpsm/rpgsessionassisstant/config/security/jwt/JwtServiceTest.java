package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zpsm.rpgsessionassisstant.exception.PlayerNotFoundException;
import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    private MockHttpServletRequest mockHttpServletRequest;
    private JwtService service;
    private Algorithm algorithm;
    private final Clock clock = Clock.fixed(Instant.parse("2023-04-03T09:00:25Z"), ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecret("secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret");
        jwtConfig.setAccessTokenExpirationAfterHours(8);
        jwtConfig.setRefreshTokenExpirationAfterHours(24);
        mockHttpServletRequest = new MockHttpServletRequest();
        algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        service = new JwtService(jwtConfig, algorithm, clock, playerRepository);
    }

    @Test
    void givenNullOrEmptyRefreshTokenShouldThrowMissingTokenException() {
        // given
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "");

        // when // then
        assertThrows(MissingTokenException.class, () -> service.refreshToken(mockHttpServletRequest, null));
    }

    @Test
    void givenExpiredTokenShouldThrowJWTVerificationException() {
        // given
        Player player = new Player();
        player.setLogin("Testowy");
        mockHttpServletRequest.addHeader(
            HttpHeaders.AUTHORIZATION,
            refreshToken(
                player,
                Date.from(Instant.ofEpochSecond(1675087968L)),
                Date.from(Instant.ofEpochSecond(1677247968L))));

        // when // then
        assertThrows(JWTVerificationException.class, () -> service.refreshToken(mockHttpServletRequest, null));
    }

    @Test
    void givenTokenWithNonExistingSubjectShouldThrowPlayerNotFoundException() {
        // given
        Instant now = clock.instant();
        mockHttpServletRequest.addHeader(
            HttpHeaders.AUTHORIZATION,
            refreshToken(new Player(), Date.from(now), refreshTokenExpiration(now)));

        // when // then
        assertThrows(PlayerNotFoundException.class, () -> service.refreshToken(mockHttpServletRequest, null));
    }

    @Test
    void givenValidRefreshTokenShouldReturnNewAccessToken() {
        // given
        Player player = new Player();
        player.setLogin("Testowy");
        Instant now = clock.instant();
        String refreshToken = refreshToken(player, Date.from(now), refreshTokenExpiration(now));
        MockHttpServletResponse expected = new MockHttpServletResponse();
        MockHttpServletResponse actual = new MockHttpServletResponse();
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, refreshToken);
        expected.addHeader(HttpHeaders.AUTHORIZATION, accessToken(player, now));
        Mockito.when(playerRepository.findByLogin(player.getLogin())).thenReturn(Optional.of(player));

        // when
        service.refreshToken(mockHttpServletRequest, actual);

        // then 2023-04-03T17:00:25Z
        assertEquals(expected.getHeader(HttpHeaders.AUTHORIZATION), actual.getHeader(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void givenPlayerShouldReturnAccessToken() {
        // given
        Player player = new Player();
        player.setLogin("Testowy");
        String expected = accessToken(player, clock.instant());

        // when
        String actual = service.accessToken(player);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenPlayerShouldReturnRefreshToken() {
        // given
        Player player = new Player();
        player.setLogin("Testowy");
        String expected = refreshToken(player, Date.from(clock.instant()), refreshTokenExpiration(clock.instant()));

        // when
        String actual = service.refreshToken(player);

        // then
        assertEquals(expected, actual);
    }

    private String accessToken(Player player, Instant now) {
        return JWT.create()
            .withSubject(player.getLogin())
            .withIssuedAt(Date.from(now))
            .withExpiresAt(accessTokenExpiration(now))
            .withClaim("authorities", player.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
            .sign(algorithm);
    }

    private String refreshToken(Player player, Date issuedAt, Date expiresAt) {
        return JWT.create()
            .withSubject(player.getLogin())
            .withIssuedAt(issuedAt)
            .withExpiresAt(expiresAt)
            .sign(algorithm);
    }

    private Date accessTokenExpiration(Instant instant) {
        return Date.from(instant.plus(Duration.ofHours(8L)));
    }

    private Date refreshTokenExpiration(Instant instant) {
        return Date.from(instant.plus(Duration.ofHours(24L)));
    }

}
