package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zpsm.rpgsessionassisstant.model.Player;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUsernameAndPasswordAuthenticationFilterTest {

    @Mock
    AuthenticationManager mockAuthenticationManager;
    MockHttpServletRequest mockRequest;

    private JwtUsernameAndPasswordAuthenticationFilter filter;
    private JwtConfig jwtConfig;
    private final Clock clock = Clock.fixed(Instant.parse("2023-03-27T16:32:25Z"), ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        jwtConfig = new JwtConfig();
        jwtConfig.setSecret("secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret");
        jwtConfig.setRefreshTokenExpirationAfterHours(8);
        jwtConfig.setAccessTokenExpirationAfterHours(24);
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        mockRequest = new MockHttpServletRequest();
        filter = new JwtUsernameAndPasswordAuthenticationFilter(
            mockAuthenticationManager,
            jwtConfig,
            algorithm,
            clock);
    }

    @Test
    void givenCorrectCredentialsShouldAuthenticate() {
        // given
        String content = """
            {
                "login": "Testowy",
                "password": "password1"
            }
            """;
        Authentication authentication = new UsernamePasswordAuthenticationToken("Testowy", "password1");
        Authentication expected = new UsernamePasswordAuthenticationToken(
            "Testowy",
            "password1",
            Set.of(new SimpleGrantedAuthority("ROLE_PLAYER")));
        mockRequest.setContent(content.getBytes());
        when(mockAuthenticationManager.authenticate(authentication)).thenReturn(expected);

        // when
        Authentication actual = filter.attemptAuthentication(mockRequest, null);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectCredentialsShouldThrowLoginException() {
        // given
        String content = """
            {
                "login": null,
                "password": ""
            }
            """;
        mockRequest.setContent(content.getBytes());

        // when // then
        assertThrows(LoginException.class, () -> filter.attemptAuthentication(mockRequest, null));
    }

    @Test
    void givenEmptyLoginPayloadShouldThrowLoginException() {
        // given
        String content = "";
        mockRequest.setContent(content.getBytes());

        // when // then
        assertThrows(LoginException.class, () -> filter.attemptAuthentication(mockRequest, null));
    }

    @Test
    void givenAuthenticatedUserShouldReturnHeaderWithTokens() throws ServletException, IOException {
        // given
        var actual = new MockHttpServletResponse();
        var expected = new MockHttpServletResponse();
        Player player = new Player();
        player.setLogin("Testowy");
        Authentication authResult = new UsernamePasswordAuthenticationToken(
            player,
            "password1",
            Set.of(new SimpleGrantedAuthority("ROLE_PLAYER")));
        expected.addHeader(HttpHeaders.AUTHORIZATION, accessToken(player, authResult));
        expected.addHeader("Refresh-Token", refreshToken(player));

        // when
        filter.successfulAuthentication(null, actual, null, authResult);

        // then
        assertEquals(expected.getHeader(HttpHeaders.AUTHORIZATION), actual.getHeader(HttpHeaders.AUTHORIZATION));
        assertEquals(expected.getHeader("Refresh-Token"), actual.getHeader("Refresh-Token"));
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
            .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
    }

    private String refreshToken(Player player) {
        return JWT.create()
            .withSubject(player.getLogin())
            .withIssuedAt(Date.from(clock.instant()))
            .withExpiresAt(refreshTokenExpiration())
            .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
    }

    private Date accessTokenExpiration() {
        return Date.from(clock.instant().plus(Duration.ofDays(jwtConfig.getAccessTokenExpirationAfterHours())));
    }

    private Date refreshTokenExpiration() {
        return Date.from(clock.instant().plus(Duration.ofDays(jwtConfig.getRefreshTokenExpirationAfterHours())));
    }

}
